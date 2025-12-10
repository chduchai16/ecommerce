package com.example.shopapp.imp_services;


import com.example.domain.models.entities.Cart;
import com.example.domain.models.entities.Order;
import com.example.domain.models.entities.User;
import com.example.domain.persistence.repositories.CartRepository;
import com.example.domain.persistence.repositories.OrderRepository;
import com.example.domain.persistence.specifications.OrderSpecification;
import com.example.domain.services.IOrderService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceIMP implements IOrderService {

    private final OrderRepository orderRepository ;
    private final CartRepository cartRepository ;

    @Override
    public Order getOrderById(int orderId) throws Exception {
        Specification<Order> spec = Specification.where(OrderSpecification.hasId(orderId));

        Optional<Order> order = orderRepository.findOne(spec);
        if(order.isEmpty()) {
            throw new EntityNotFoundException("Không tìm thấy đơn hàng này");
        }
        return order.get();
    }

    @Override
    public Page<Order> getOrdersByUserId(int userId, Pageable pageable) {
        Specification<Order> spec = Specification.where(OrderSpecification.hasUserId(userId));
        Page<Order> order = orderRepository.findAll(spec, pageable);
        return order ;
    }

    @Override
    public Page<Order> filterOrders(
            Float minTotalAmount ,
            Float maxTotalAmount ,
            Integer status ,
            String shippingAddress ,
            String customerName ,
            Pageable pageable
    ) {
        Specification<Order> spec = Specification.where(OrderSpecification.minTotalAmount(minTotalAmount))
                .and(OrderSpecification.maxTotalAmount(maxTotalAmount))
                .and(OrderSpecification.hasStatus(status))
                .and(OrderSpecification.hasShippingAddress(shippingAddress))
                .and(OrderSpecification.hasCustomerName(customerName));
        return orderRepository.findAll(spec, pageable);
    }

    @Transactional(rollbackOn = Exception.class)
    @Override
    public Order createOrder(Order order) throws Exception {
        User user = order.getUser();
        if (user == null || user.getId() == null) {
            throw new Exception("User không được để trống khi tạo đơn hàng");
        }

        Cart cart = user.getCart();
        if (cart == null) {
            throw new Exception("User chưa có giỏ hàng");
        }
        Order savedOrder = orderRepository.save(order);
        cart.getCartItems().clear();
        cartRepository.save(cart);
        return savedOrder;
    }

    @Override
    public Order updateOrder(Order order) throws Exception {
        if(order.getId() == null ) {
            throw new Exception("Id không được để trống khi cập nhật");
        }

        Specification<Order> spec = Specification.where(OrderSpecification.hasId(order.getId()));
        boolean exists = orderRepository.exists(spec) ;
        if(!exists) {
            throw new EntityNotFoundException("Không tìm thấy đơn hàng này");
        }
        return this.orderRepository.save(order);
    }

    @Override
    public void deleteOrder(int orderId) throws Exception {
        Specification<Order> spec = Specification.where(OrderSpecification.hasId(orderId));
        boolean exists = orderRepository.exists(spec) ;
        if(!exists) {
            throw new EntityNotFoundException("Không tìm thấy đơn hàng này");
        }
        orderRepository.deleteById(orderId);
    }

    public Page<Order> filterOrdersBySeller(
            Integer sellerId,
            Float minTotalAmount,
            Float maxTotalAmount,
            Integer status,
            String shippingAddress,
            String customerName,
            PageRequest pageRequest
    ) {
        // Lấy tất cả Order, sau đó filter trong memory
        // HOẶC dùng @Query custom để filter trong database

        Specification<Order> spec = Specification.where(
                OrderSpecification.hasSellerId(sellerId)
        );

        if (minTotalAmount != null) {
            spec = spec.and(OrderSpecification.minTotalAmount(minTotalAmount));
        }
        if (maxTotalAmount != null) {
            spec = spec.and(OrderSpecification.maxTotalAmount(maxTotalAmount));
        }
        if (status != null) {
            spec = spec.and(OrderSpecification.hasStatus(status));
        }
        if (shippingAddress != null && !shippingAddress.isEmpty()) {
            spec = spec.and(OrderSpecification.hasShippingAddress(shippingAddress));
        }
        if (customerName != null && !customerName.isEmpty()) {
            spec = spec.and(OrderSpecification.hasCustomerName(customerName));
        }

        return orderRepository.findAll(spec, pageRequest);
    }
}

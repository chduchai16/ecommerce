package com.example.shopapp.imp_services;


import com.example.domain.models.entities.Order;
import com.example.domain.persistence.repositories.OrderRepository;
import com.example.domain.persistence.specifications.OrderSpecification;
import com.example.domain.services.IOrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceIMP implements IOrderService {

    private final OrderRepository orderRepository ;

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
    public Page<Order> filterOrders(Float minTotalAmount , Float maxTotalAmount , Integer status , String shippingAddress , String customerName ,Pageable pageable) {
        Specification<Order> spec = Specification.where(OrderSpecification.minTotalAmount(minTotalAmount))
                .and(OrderSpecification.maxTotalAmount(maxTotalAmount))
                .and(OrderSpecification.hasStatus(status))
                .and(OrderSpecification.hasShippingAddress(shippingAddress))
                .and(OrderSpecification.hasCustomerName(customerName));
        return orderRepository.findAll(spec, pageable);
    }

    @Override
    public Order createOrder(Order order) throws Exception {
        return orderRepository.save(order);
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


//    @Override
//    public Order placeOrder(Order order) throws Exception {
//        Order order = new Order() ;
//        // kiểm tra sự tồn tại của user
//        User user = userRepository.findById(orderRequest.getUserId()).orElseThrow(()->new EntityNotFoundException("Cannot find user:" + orderRequest.getUserId()));
//        // tìm products theo ids
//        List<Integer> productIds = orderRequest.getOrderRequestItems().stream().map(OrderRequestItem::getProductId).toList() ;
//        List<Product> products = productRepository.findAllById(productIds) ;
//        // tạo map id - product
//        // Function.identity() truyền chính giá trị nó lấy mà khônng thay đổi gì
//        Map<Integer, Product> productMap = products.stream().collect(Collectors.toMap(Product::getId, Function.identity()));
//
//        for (OrderRequestItem item : orderRequest.getOrderRequestItems()) {
//            Product product = productMap.get(item.getProductId());
//            if (product == null) {
//                throw new EntityNotFoundException("Sản phẩm ID " + item.getProductId() + " không tồn tại");
//            }
//            if (product.getStockQuantity() < item.getQuantity()) {
//                throw new Exception("Không đủ hàng cho sản phẩm " + product.getName());
//            }
//            product.setStockQuantity(product.getStockQuantity() - item.getQuantity());
//        }
//        // cập nhật số lượng trong kho
//        productRepository.saveAll(products) ;
//
//        order.setFullName(orderRequest.getFullname());
//        order.setEmail(orderRequest.getEmail());
//        order.setStatus(OrderStatus.PENDING.toString());
//        order.setNote(orderRequest.getNote());
//        order.setPhoneNumber(orderRequest.getPhoneNumber());
//        order.setEmail(orderRequest.getEmail());
//        order.setShippingAddress(orderRequest.getShippingAddress());
//        order.setShippingMethod(orderRequest.getShippingMethod());
//        order.setPaymentMethod(orderRequest.getPaymentMethod());
//        order.setTotalPrice(orderRequest.getTotalPrice());
//        order.setUser(user);
//
//        List<OrderDetail> orderDetails = new ArrayList<>() ;
//        // duyệt qua order item khách đặt
//        for (OrderRequestItem item : orderRequest.getOrderRequestItems()) {
//            // lấy product theo order item
//            Product product = productMap.get(item.getProductId());
//            OrderDetail orderDetail = new OrderDetail();
//            orderDetail.setOrder(order);
//            orderDetail.setProduct(product);
//            orderDetail.setQuantity(item.getQuantity());
//            orderDetail.setTotal(item.getQuantity() * product.getPrice());
//            orderDetails.add(orderDetail);
//        }
//        // gắn danh sách order detail vào order
//        order.setOrderDetails(orderDetails);
//        orderRepository.save(order) ;
//        return order ;
//    }
}

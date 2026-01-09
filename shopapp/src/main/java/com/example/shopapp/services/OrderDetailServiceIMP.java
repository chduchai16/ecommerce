package com.example.shopapp.services;

import com.example.shopapp.models.entities.OrderDetail;
import com.example.shopapp.repositories.OrderDetailRepository;
import com.example.shopapp.specifications.OrderDetailSpecification;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceIMP implements IOrderDetailService {

    private final OrderDetailRepository orderDetailRepository ;

    @Override
    public OrderDetail getOrderDetailById(int orderDetailId) throws Exception {

        Specification<OrderDetail> spec = Specification.where(OrderDetailSpecification.hasId(orderDetailId));

        Optional<OrderDetail> orderDetail = orderDetailRepository.findOne(spec);
        if(orderDetail.isEmpty()){
            throw new EntityNotFoundException("Không tìm thấy chi tiết đơn hàng này");
        }
        return orderDetail.get();
    }

    @Override
    public OrderDetail createOrderDetail(OrderDetail orderDetail) throws Exception {
        return this.orderDetailRepository.save(orderDetail);
    }

    @Override
    public OrderDetail updateOrderDetail(OrderDetail orderDetail) throws Exception {
        if(orderDetail.getId() == null) {
            throw new Exception("Id không được để trống khi cập nhật");
        }

        Specification<OrderDetail> spec = Specification.where(OrderDetailSpecification.hasId(orderDetail.getId()));

        boolean existsById = orderDetailRepository.exists(spec);
        if(!existsById) {
            throw new EntityNotFoundException("Chi tiết đơn hàng này không tồn tại");
        }
        return this.orderDetailRepository.save(orderDetail);
    }

    @Override
    public void deleteOrderDetail(int orderDetailId) throws Exception {

        Specification<OrderDetail> spec = Specification.where(OrderDetailSpecification.hasId(orderDetailId));

        boolean existsById = orderDetailRepository.exists(spec);
        if(!existsById) {
            throw new EntityNotFoundException("Chi tiết đơn hàng này không tồn tại");
        }
        orderDetailRepository.deleteById(orderDetailId);
    }
}

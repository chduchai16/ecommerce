package com.example.shopapp.imp_services;

import com.example.domain.models.entities.OrderDetail;
import com.example.domain.persistence.repositories.OrderDetailRepository;
import com.example.domain.services.IOrderDetailService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderDetailServiceIMP implements IOrderDetailService {

    private final OrderDetailRepository orderDetailRepository ;

    @Override
    public OrderDetail getOrderDetailById(int orderDetailId) throws Exception {
        return orderDetailRepository.findById(orderDetailId).orElseThrow(()->new EntityNotFoundException("Không tìm thấy chi tiết đơn hàng này"));
    }

    @Override
    public List<OrderDetail> getAllOrderDetails() {
        return orderDetailRepository.findAll();
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
        if(this.orderDetailRepository.findById(orderDetail.getId()).isEmpty()) {
            throw new EntityNotFoundException("Chi tiết đơn hàng này không tồn tại");
        }
        return this.orderDetailRepository.save(orderDetail);
    }

    @Override
    public void deleteOrderDetail(int orderDetailId) throws Exception {
        if (orderDetailRepository.findById(orderDetailId).isEmpty()){
            throw new EntityNotFoundException("Chi tiết đơn hàng này không tồn tại");
        }
        else {
            orderDetailRepository.deleteById(orderDetailId);
        }

    }
}

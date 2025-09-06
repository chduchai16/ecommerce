package com.example.exona_tech.imp_services;

import com.example.domain.entities.OrderDetail;
import com.example.domain.repositories.OrderDetailRepository;
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
        return orderDetailRepository.findById(orderDetailId).orElseThrow(()->new Exception("Cannot fiind this order detail"));
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
            throw new Exception("Id must not be null to update");
        }
        if(this.orderDetailRepository.findById(orderDetail.getId()).isEmpty()) {
            throw new EntityNotFoundException("This order detail does not exist");
        }
        return this.orderDetailRepository.save(orderDetail);
    }

    @Override
    public void deleteOrderDetail(int orderDetailId) throws Exception {
        if (orderDetailRepository.findById(orderDetailId).isEmpty()){
            throw new Exception("This order detail does not exist");
        }
        else {
            orderDetailRepository.deleteById(orderDetailId);
        }

    }
}

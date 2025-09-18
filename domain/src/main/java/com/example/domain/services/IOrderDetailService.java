package com.example.domain.services;

import com.example.domain.models.entities.OrderDetail;

public interface IOrderDetailService {
    OrderDetail getOrderDetailById(int orderDetailId) throws Exception;
    OrderDetail createOrderDetail(OrderDetail orderDetail) throws Exception;
    OrderDetail updateOrderDetail(OrderDetail orderDetail) throws Exception;
    void deleteOrderDetail(int orderDetailId) throws Exception;
}

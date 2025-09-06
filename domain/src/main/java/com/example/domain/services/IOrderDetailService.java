package com.example.domain.services;

import com.example.domain.entities.OrderDetail;
import java.util.List;

public interface IOrderDetailService {
    OrderDetail getOrderDetailById(int orderDetailId) throws Exception;
    List<OrderDetail> getAllOrderDetails();
    OrderDetail createOrderDetail(OrderDetail orderDetail) throws Exception;
    OrderDetail updateOrderDetail(OrderDetail orderDetail) throws Exception;
    void deleteOrderDetail(int orderDetailId) throws Exception;
}

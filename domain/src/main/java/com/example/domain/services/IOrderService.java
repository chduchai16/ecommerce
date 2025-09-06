package com.example.domain.services;

import com.example.domain.entities.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IOrderService {
    Order getOrderById(int orderId) throws Exception;
    Page<Order> getAllOrders(Pageable pageable);
    Order createOrder(Order order) throws Exception;
    Order updateOrder(Order order) throws Exception;
    void deleteOrder(int orderId) throws Exception;
    Page<Order> getOrdersByUserId(int userId , Pageable pageable) ;
}

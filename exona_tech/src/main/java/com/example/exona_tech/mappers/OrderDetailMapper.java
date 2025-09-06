package com.example.exona_tech.mappers;

import com.example.domain.dtos.requests.OrderDetailDTO;
import com.example.domain.entities.Order;
import com.example.domain.entities.OrderDetail;
import com.example.domain.entities.Product;
import com.example.domain.repositories.OrderRepository;
import com.example.domain.repositories.ProductRepository;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderDetailMapper {
    private final ProductRepository productRepository ;
    private final OrderRepository orderRepository ;
    private final ModelMapper modelMapper ;

    private TypeMap<OrderDetailDTO , OrderDetail> fromRequestToEntityTypeMap;

    public OrderDetail fromRequestToEntity (OrderDetailDTO orderDetailDTO) {
        if(orderDetailDTO == null) return null ;
        if (fromRequestToEntityTypeMap == null ){
            fromRequestToEntityTypeMap = modelMapper.createTypeMap(OrderDetailDTO.class ,OrderDetail.class) ;
            fromRequestToEntityTypeMap.getMappings().clear() ;
            fromRequestToEntityTypeMap.addMappings(mapper -> {
                mapper.skip(OrderDetail :: setOrder);
                mapper.skip(OrderDetail :: setProduct);
            });
            fromRequestToEntityTypeMap.implicitMappings();
        }

        OrderDetail orderDetail = fromRequestToEntityTypeMap.map(orderDetailDTO) ;

        // map order
        if(orderDetailDTO.getOrderId() != null) {
            Order order = orderRepository.findById(orderDetailDTO.getOrderId()).orElseThrow(()-> new EntityNotFoundException("This order does not exist"));
            orderDetail.setOrder(order);
        }
        // map product
        if (orderDetailDTO.getProductId() != null) {
            Product product = productRepository.findById(orderDetailDTO.getProductId()).orElseThrow(()-> new EntityNotFoundException("This product does not exist"));
            orderDetail.setProduct(product);
        }

        return orderDetail ;
    }
}

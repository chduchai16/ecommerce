package com.example.exona_tech.mappers;

import com.example.domain.dtos.requests.OrderDTO;
import com.example.domain.dtos.resposnes.OrderDetailResponse;
import com.example.domain.dtos.resposnes.OrderResponse;
import com.example.domain.entities.Order;
import com.example.domain.entities.OrderDetail;
import com.example.domain.entities.User;
import com.example.domain.repositories.OrderDetailRepository;
import com.example.domain.repositories.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderMapper {
    private final ModelMapper modelMapper ;
    private final UserRepository userRepository ;
    private final OrderDetailRepository orderDetailRepository ;
    private final OrderDetailMapper orderDetailMapper ;

    private TypeMap<OrderDTO , Order> fromRequestToEntityTypeMap ;
    private TypeMap<Order , OrderResponse> fromEntityToResponseTypeMap ;

    public Order fromRequestToEntity(OrderDTO orderDTO) {
        if(orderDTO == null) return null ;
        if (fromRequestToEntityTypeMap == null) {
            fromRequestToEntityTypeMap = modelMapper.createTypeMap(OrderDTO.class , Order.class);
            fromRequestToEntityTypeMap.getMappings().clear();
            fromRequestToEntityTypeMap.addMappings(mapper -> {
                mapper.skip(Order :: setOrderDetails);
                mapper.skip(Order :: setUser);
            });
            fromRequestToEntityTypeMap.implicitMappings();
        }

        Order order = fromRequestToEntityTypeMap.map(orderDTO);

        // map user
        if(orderDTO.getUserId() != null) {
            User user = this.userRepository.findById(orderDTO.getUserId()).orElseThrow(()->new EntityNotFoundException("This user does not exist"));
            order.setUser(user);
        }
        // map order detail
        if(orderDTO.getOrderDetailIds() != null && !orderDTO.getOrderDetailIds().isEmpty()) {
            List<OrderDetail> orderDetails = orderDetailRepository.findAllById(orderDTO.getOrderDetailIds());
            order.setOrderDetails(orderDetails);
        }

        return order ;
    }

    public OrderResponse fromEntityToResponse (Order order){
        if(order == null) return null ;
        if(fromEntityToResponseTypeMap == null){
            fromEntityToResponseTypeMap = modelMapper.createTypeMap(Order.class , OrderResponse.class);
            fromEntityToResponseTypeMap.getMappings().clear();
            fromEntityToResponseTypeMap.addMappings(mapper -> {
                mapper.skip(OrderResponse :: setUserId);
                mapper.skip(OrderResponse :: setOrderDetailResponses);
            });
            fromEntityToResponseTypeMap.implicitMappings();
        }
        OrderResponse orderResponse = fromEntityToResponseTypeMap.map(order) ;

        // map user id
        if(order.getUser() != null){
            orderResponse.setUserId(order.getUser().getId());
        }
        // map order detail ids
        if(order.getOrderDetails() != null && !order.getOrderDetails().isEmpty()){
            List<OrderDetailResponse> orderDetailResponses = order.getOrderDetails()
                    .stream()
                    .map(orderDetailMapper::fromEntityToResponse)
                    .toList() ;
            orderResponse.setOrderDetailResponses(orderDetailResponses);
        }

        return orderResponse ;
    }
}

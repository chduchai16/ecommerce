package com.example.shopapp.transfer.mappers;

import com.example.domain.models.entities.Order;
import com.example.domain.models.entities.OrderDetail;
import com.example.domain.models.entities.User;
import com.example.domain.persistence.repositories.CouponRepository;
import com.example.domain.persistence.repositories.OrderDetailRepository;
import com.example.domain.persistence.repositories.UserRepository;
import com.example.shopapp.imp_services.CouponServiceIMP;
import com.example.shopapp.transfer.dtos.requests.OrderDTO;
import com.example.shopapp.transfer.dtos.responses.OrderDetailResponse;
import com.example.shopapp.transfer.dtos.responses.OrderResponse;
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
    private final CouponRepository couponRepository ;

    private TypeMap<OrderDTO, Order> fromRequestToEntityTypeMap ;
    private TypeMap<Order , OrderResponse> fromEntityToResponseTypeMap ;

    public Order fromRequestToEntity(OrderDTO orderDTO) {
        if(orderDTO == null) return null ;
        if (fromRequestToEntityTypeMap == null) {
            fromRequestToEntityTypeMap = modelMapper.createTypeMap(OrderDTO.class , Order.class);
            fromRequestToEntityTypeMap.getMappings().clear();
            fromRequestToEntityTypeMap.addMappings(mapper -> {
                mapper.skip(Order :: setOrderDetails);
                mapper.skip(Order :: setUser);
                mapper.skip(Order :: setCoupon);
            });
            fromRequestToEntityTypeMap.implicitMappings();
        }

        Order order = fromRequestToEntityTypeMap.map(orderDTO);

        // map user
        if(orderDTO.getUserId() != null) {
            User user = this.userRepository.findById(orderDTO.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("Người dùng với id " + orderDTO.getUserId() + " không tồn tại"));
            order.setUser(user);
        }

        // map coupon
        if(orderDTO.getCouponCode() != null && !orderDTO.getCouponCode().isEmpty()) {

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
                mapper.skip(OrderResponse :: setCouponCode);
            });
            fromEntityToResponseTypeMap.implicitMappings();
        }
        OrderResponse orderResponse = fromEntityToResponseTypeMap.map(order) ;

        // map user id
        if(order.getUser() != null){
            orderResponse.setUserId(order.getUser().getId());
        }

        // map coupon code
        if(order.getCoupon() != null){
            orderResponse.setCouponCode(order.getCoupon().getCode());
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

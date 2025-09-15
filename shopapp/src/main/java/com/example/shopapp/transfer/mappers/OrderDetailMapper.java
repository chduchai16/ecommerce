package com.example.shopapp.transfer.mappers;

import com.example.domain.models.entities.Order;
import com.example.domain.models.entities.OrderDetail;
import com.example.domain.models.entities.Product;
import com.example.domain.persistence.repositories.OrderRepository;
import com.example.domain.persistence.repositories.ProductRepository;
import com.example.shopapp.transfer.dtos.requests.OrderDetailDTO;
import com.example.shopapp.transfer.dtos.responses.OrderDetailResponse;
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
    private final ProductMapper productMapper ;
    private TypeMap<OrderDetailDTO, OrderDetail> fromRequestToEntityTypeMap;
    private TypeMap<OrderDetail , OrderDetailResponse> fromEntityToResponseTypeMap ;

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
            Order order = orderRepository.findById(orderDetailDTO.getOrderId())
                    .orElseThrow(() -> new EntityNotFoundException("Đơn hàng với id " + orderDetailDTO.getOrderId() + " không tồn tại"));
            orderDetail.setOrder(order);
        }
        // map product
        if (orderDetailDTO.getProductId() != null) {
            Product product = productRepository.findById(orderDetailDTO.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Sản phẩm với id " + orderDetailDTO.getProductId() + " không tồn tại"));
            orderDetail.setProduct(product);
        }

        return orderDetail ;
    }

    public OrderDetailResponse fromEntityToResponse (OrderDetail orderDetail){
        if(orderDetail == null) return null ;
        if(fromEntityToResponseTypeMap == null){
            fromEntityToResponseTypeMap = modelMapper.createTypeMap(OrderDetail.class , OrderDetailResponse.class);
            fromEntityToResponseTypeMap.getMappings().clear();
            fromEntityToResponseTypeMap.addMappings(mapper -> {
                mapper.skip(OrderDetailResponse :: setProductResponse);
                mapper.skip(OrderDetailResponse :: setOrderId);
            });
            fromEntityToResponseTypeMap.implicitMappings();
        }

        OrderDetailResponse orderDetailResponse = fromEntityToResponseTypeMap.map(orderDetail);
        // map product response
        if(orderDetail.getProduct() != null) {
            orderDetailResponse.setProductResponse(productMapper.fromEntityToResponse(orderDetail.getProduct()));
        }

        // map order id
        if(orderDetail.getOrder() != null) {
            orderDetailResponse.setOrderId(orderDetail.getOrder().getId());
        }

        return orderDetailResponse ;
    }
}

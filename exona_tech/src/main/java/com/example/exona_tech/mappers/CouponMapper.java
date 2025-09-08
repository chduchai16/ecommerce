package com.example.exona_tech.mappers;

import com.example.exona_tech.dtos.requests.CouponDTO;
import com.example.exona_tech.dtos.resposnes.CouponResponse;
import com.example.domain.entities.Coupon;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CouponMapper {
    private final ModelMapper modelMapper ;

    private TypeMap<CouponDTO , Coupon> fromRequestToEntityTypeMap ;
    private TypeMap < Coupon , CouponResponse > fromEntityToResponseTypeMap ;

    public Coupon fromRequestToEntity (CouponDTO couponDTO) {
        if (couponDTO == null) return null ;
        if (fromRequestToEntityTypeMap== null) {
            fromRequestToEntityTypeMap = modelMapper.createTypeMap(CouponDTO.class ,Coupon.class) ;
            fromRequestToEntityTypeMap.getMappings().clear();
            fromRequestToEntityTypeMap.implicitMappings();
        }

        return fromRequestToEntityTypeMap.map(couponDTO);
    }

    public CouponResponse fromEntityToResponse (Coupon coupon){
        if(coupon == null) return null ;
        if(fromEntityToResponseTypeMap == null){
            fromEntityToResponseTypeMap = modelMapper.createTypeMap(Coupon.class , CouponResponse.class);
            fromEntityToResponseTypeMap.getMappings().clear();
            fromEntityToResponseTypeMap.implicitMappings();
        }
        return fromEntityToResponseTypeMap.map(coupon) ;
    }
}

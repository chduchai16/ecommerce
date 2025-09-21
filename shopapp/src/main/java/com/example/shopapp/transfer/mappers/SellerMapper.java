package com.example.shopapp.transfer.mappers;

import com.example.domain.models.entities.Seller;
import com.example.shopapp.transfer.dtos.requests.SellerDTO;
import com.example.shopapp.transfer.dtos.responses.SellerResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SellerMapper {

    private final ModelMapper modelMapper;

    public Seller fromRequestToEntity(SellerDTO sellerDTO) {
        if (sellerDTO == null)
            return null;
        return modelMapper.map(sellerDTO, Seller.class);
    }

    public SellerResponse fromEntityToResponse(Seller seller) {
        if (seller == null)
            return null;
        return modelMapper.map(seller, SellerResponse.class);
    }
}
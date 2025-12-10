package com.example.shopapp.transfer.mappers;

import com.example.domain.models.entities.InventoryHistory;
import com.example.shopapp.transfer.dtos.responses.InventoryHistoryResponse;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InventoryHistoryMapper {
    private final ModelMapper modelMapper;

    private TypeMap<InventoryHistory, InventoryHistoryResponse> fromEntityToResponseTypeMap;

    public InventoryHistoryResponse fromEntityToResponse(InventoryHistory inventoryHistory) {
        if (inventoryHistory == null)
            return null;

        if (fromEntityToResponseTypeMap == null) {
            fromEntityToResponseTypeMap = this.modelMapper.createTypeMap(InventoryHistory.class, InventoryHistoryResponse.class);
            fromEntityToResponseTypeMap.getMappings().clear();
            fromEntityToResponseTypeMap.addMappings(mapper -> {
                mapper.map(src -> src.getProduct().getId(), InventoryHistoryResponse::setProductId);
                mapper.map(src -> src.getProduct().getName(), InventoryHistoryResponse::setProductName);
                mapper.map(src -> src.getPerformedBy().getFullName(), InventoryHistoryResponse::setPerformedBy);
            });
        }

        return fromEntityToResponseTypeMap.map(inventoryHistory);
    }
}


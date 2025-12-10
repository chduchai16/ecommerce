package com.example.shopapp.services;

import com.example.shopapp.models.entities.InventoryHistory;
import com.example.shopapp.models.entities.Product;
import com.example.shopapp.models.entities.User;
import com.example.shopapp.models.enums.InventoryStats;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;

public interface IInventoryService {
    Page<Product> getSellerInventory(
            Integer sellerId,
            String search,
            Integer status,
            PageRequest pageRequest
    ) throws Exception;

    InventoryStats getSellerInventoryStats(Integer sellerId) throws Exception;

    Product importStock(
            Integer productId,
            Integer quantity,
            String note,
            User performedBy
    ) throws Exception;

    Product updateProductStock(
            Integer productId,
            Integer newQuantity,
            User performedBy,
            String reason
    ) throws Exception;

    Product exportStock(
            Integer productId,
            Integer quantity,
            String reason,
            User performedBy
    ) throws Exception;


    Long getProductTotalSold(Integer productId) throws Exception;

    Page<InventoryHistory> getSellerInventoryHistory(
            Integer sellerId,
            PageRequest pageRequest
    ) throws Exception;

    List<InventoryHistory> getProductHistory(Integer productId) throws Exception;

    Page<InventoryHistory> getSellerImportHistory(
            Integer sellerId,
            PageRequest pageRequest
    ) throws Exception;


    Boolean isWarningStock(Integer productId) throws Exception;

}


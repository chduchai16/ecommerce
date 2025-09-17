package com.example.domain.services;

import com.example.domain.models.entities.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ISupplierService {
    Supplier getSupplierById(int supplierId) throws Exception;
    Page<Supplier> filterSuppliers(String name , String phoneNumber , String email , String address ,Pageable pageable);
    Supplier createSupplier(Supplier supplier) throws Exception;
    Supplier updateSupplier(Supplier supplier) throws Exception;
    void deleteSupplier(int supplierId) throws Exception;
}

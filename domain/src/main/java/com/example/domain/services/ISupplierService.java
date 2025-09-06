package com.example.domain.services;

import com.example.domain.entities.Product;
import com.example.domain.entities.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ISupplierService {
    Supplier getSupplierById(int supplierId) throws Exception;
    Page<Supplier> getAllSuppliers(Pageable pageable);
    Supplier createSupplier(Supplier supplier) throws Exception;
    Supplier updateSupplier(Supplier supplier) throws Exception;
    void deleteSupplier(int supplierId) throws Exception;
    List<Product> getProductsBySupplierId(int supplierId);
}

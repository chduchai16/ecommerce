package com.example.exona_tech.imp_services;

import com.example.domain.entities.Product;
import com.example.domain.entities.Supplier;
import com.example.domain.repositories.SupplierRepository;
import com.example.domain.services.ISupplierService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class SupplierServiceIMP implements ISupplierService {

    private final SupplierRepository supplierRepository ;

    @Override
    public Supplier getSupplierById(int supplierId) throws Exception {
        return supplierRepository.findById(supplierId).orElseThrow(()->new Exception("Cannot find this supplier"));
    }

    @Override
    public Page<Supplier> getAllSuppliers(Pageable pageable) {
        return supplierRepository.findAll(pageable);
    }

    @Override
    public Supplier createSupplier(Supplier supplier) throws Exception {
        return supplierRepository.save(supplier) ;
    }

    @Override
    public Supplier updateSupplier(Supplier supplier) throws Exception {
        if (supplier.getId() == null) {
            throw new Exception("Id must not be null to update");
        }
        else if (!this.supplierRepository.existsById(supplier.getId())){
            throw new EntityNotFoundException("This supplier does not exist");
        }
        return this.supplierRepository.save(supplier) ;
    }

    @Override
    public void deleteSupplier(int supplierId) throws Exception {
        if (supplierRepository.findById(supplierId).isEmpty()){
            throw new Exception("This supplier does not exist");
        }
        supplierRepository.deleteById(supplierId);
    }

    @Override
    public List<Product> getProductsBySupplierId(int supplierId) {
        return supplierRepository.findProductsById(supplierId) ;
    }
}

package com.example.shopapp.imp_services;

import com.example.domain.models.entities.Product;
import com.example.domain.models.entities.Supplier;
import com.example.domain.persistence.repositories.SupplierRepository;
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
        return supplierRepository.findById(supplierId).orElseThrow(()->new EntityNotFoundException("Không tìm thấy nhà cung cấp này"));
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
            throw new Exception("Id không được để trống khi cập nhật");
        }
        else if (!this.supplierRepository.existsById(supplier.getId())){
            throw new EntityNotFoundException("Nhà cung cấp này không tồn tại");
        }
        return this.supplierRepository.save(supplier) ;
    }

    @Override
    public void deleteSupplier(int supplierId) throws Exception {
        if (supplierRepository.findById(supplierId).isEmpty()){
            throw new EntityNotFoundException("Nhà cung cấp này không tồn tại");
        }
        supplierRepository.deleteById(supplierId);
    }

    @Override
    public List<Product> getProductsBySupplierId(int supplierId) {
        return supplierRepository.findProductsById(supplierId) ;
    }
}

package com.example.shopapp.imp_services;

import com.example.domain.models.entities.Supplier;
import com.example.domain.persistence.repositories.SupplierRepository;
import com.example.domain.persistence.specifications.SupplierSpecification;
import com.example.domain.services.ISupplierService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SupplierServiceIMP implements ISupplierService {

    private final SupplierRepository supplierRepository ;

    @Override
    public Supplier getSupplierById(int supplierId) throws Exception {
        Specification<Supplier> spec = Specification.where(SupplierSpecification.hasId(supplierId));
        Optional<Supplier> existingSupplier = supplierRepository.findOne(spec);
        if (existingSupplier.isEmpty()) {
            throw new EntityNotFoundException("Nhà cung cấp không tồn tại");
        }
        return existingSupplier.get();
    }

    @Override
    public Page<Supplier> filterSuppliers(String name, String phoneNumber, String email, String address, Pageable pageable) {
        Specification<Supplier> spec = Specification.where(SupplierSpecification.hasName(name))
                .and(SupplierSpecification.hasPhoneNumber(phoneNumber))
                .and(SupplierSpecification.hasEmail(email))
                .and(SupplierSpecification.hasAddress(address));
        return supplierRepository.findAll(spec, pageable);
    }

    @Override
    public Supplier createSupplier(Supplier supplier) throws Exception {
        // kiểm tra tên nhà cung cấp không được để trống
        if (supplier.getName() == null || supplier.getName().isEmpty()) {
            throw new Exception("Tên nhà cung cấp không được để trống");
        }
        // kiểm tra số điện thoại không được để trống
        if (supplier.getPhoneNumber() == null || supplier.getPhoneNumber().isEmpty()) {
            throw new Exception("Số điện thoại không được để trống");
        }
        // kiểm tra địa chỉ không được để trống
        if (supplier.getAddress() == null || supplier.getAddress().isEmpty()) {
            throw new Exception("Địa chỉ không được để trống");
        }
        Specification<Supplier> spec = Specification.where(SupplierSpecification.hasExactName(supplier.getName()))
                .or(SupplierSpecification.hasExactPhoneNumber(supplier.getPhoneNumber()))
                .or(SupplierSpecification.hasExactEmail(supplier.getEmail()))
                .or(SupplierSpecification.hasExactAddress(supplier.getAddress()));

        boolean exists = supplierRepository.exists(spec);
        if (exists) {
            throw new Exception("Nhà cung cấp đã bị trùng thông tin nào đó");
        }
        return this.supplierRepository.save(supplier) ;
    }

    @Override
    public Supplier updateSupplier(Supplier supplier) throws Exception {
        if(supplier.getId() == null) {
            throw new Exception("Mã nhà cung cấp không được để trống khi cập nhật");
        }
        if (supplier.getName() == null || supplier.getName().isEmpty()) {
            throw new Exception("Tên nhà cung cấp không được để trống");
        }
        // kiểm tra số điện thoại không được để trống
        if (supplier.getPhoneNumber() == null || supplier.getPhoneNumber().isEmpty()) {
            throw new Exception("Số điện thoại không được để trống");
        }
        // kiểm tra địa chỉ không được để trống
        if (supplier.getAddress() == null || supplier.getAddress().isEmpty()) {
            throw new Exception("Địa chỉ không được để trống");
        }
        Specification<Supplier> spec = Specification.where(SupplierSpecification.hasExactName(supplier.getName()))
                .or(SupplierSpecification.hasExactPhoneNumber(supplier.getPhoneNumber()))
                .or(SupplierSpecification.hasExactEmail(supplier.getEmail()))
                .or(SupplierSpecification.hasExactAddress(supplier.getAddress()))
                .and(SupplierSpecification.hasId(supplier.getId())) ;
        boolean exists = supplierRepository.exists(spec);
        if (exists) {
            throw new Exception("Nhà cung cấp đã bị trùng thông tin nào đó");
        }
        return this.supplierRepository.save(supplier) ;
    }

    @Override
    public void deleteSupplier(int supplierId) throws Exception {

        Specification<Supplier> spec = Specification.where(SupplierSpecification.hasId(supplierId));
        Optional<Supplier> existingSupplier = supplierRepository.findOne(spec);
        if (existingSupplier.isEmpty()) {
            throw new EntityNotFoundException("Nhà cung cấp không tồn tại");
        }
        supplierRepository.deleteById(supplierId);
    }
}

package com.example.domain.repositories;

import com.example.domain.entities.Product;
import com.example.domain.entities.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier,Integer> {
    List<Product> findProductsById(int id);

    Optional<Supplier> findByPhoneNumber(String phoneNumber);

    Page<Supplier> findAll(Pageable pageable);
}
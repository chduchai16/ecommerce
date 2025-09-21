package com.example.domain.persistence.repositories;

import com.example.domain.models.entities.Seller;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Integer>, JpaSpecificationExecutor<Seller> {
}
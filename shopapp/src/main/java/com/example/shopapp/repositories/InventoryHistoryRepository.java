package com.example.shopapp.repositories;

import com.example.shopapp.models.entities.InventoryHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryHistoryRepository extends JpaRepository<InventoryHistory, Integer>,
                                                     JpaSpecificationExecutor<InventoryHistory> {
}


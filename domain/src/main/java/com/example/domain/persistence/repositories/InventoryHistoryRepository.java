package com.example.domain.persistence.repositories;

import com.example.domain.models.entities.InventoryHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryHistoryRepository extends JpaRepository<InventoryHistory, Integer>,
                                                     JpaSpecificationExecutor<InventoryHistory> {
}


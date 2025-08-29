package com.example.domain.repositories;

import com.example.domain.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findByPhoneNumber(String phoneNumber) ;
    Page<User> findAll (Pageable pageable) ;

    @Query("SELECT u FROM User u WHERE (:keyword IS NULL OR :keyword = '' OR u.fullName LIKE CONCAT('%', :keyword, '%') OR u.phoneNumber LIKE CONCAT('%', :keyword, '%') OR u.email LIKE CONCAT('%', :keyword, '%') OR u.address LIKE CONCAT('%', :keyword, '%'))")
    Page<User> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

}

package com.example.domain.repositories;

import com.example.domain.entities.Product;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> , JpaSpecificationExecutor<Product> {

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.averageRating = (SELECT COALESCE(AVG(r.rate), 0) FROM Rating r WHERE r.product.id = :productId) WHERE p.id = :productId")
    void updateAverageRating(@Param("productId") int productId);

    Page<Product> findAll(Pageable pageable);

    @Query("SELECT p FROM Product p WHERE :keyword IS NULL OR :keyword = '' OR p.name LIKE CONCAT('%', :keyword, '%') OR p.brand LIKE CONCAT('%', :keyword, '%') OR p.category.name LIKE CONCAT('%', :keyword, '%')")
    Page<Product> searchByKeyword(
            @Param("keyword") String keyword,
            Pageable pageable
    );

    @Query("SELECT p FROM Product p WHERE (:keyword IS NULL OR :keyword = '') OR p.name LIKE CONCAT('%', :keyword, '%') OR p.brand LIKE CONCAT('%', :keyword, '%') OR p.description LIKE CONCAT('%' , :keyword, '%')")
    Page<Product> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    Page<Product> findByCategoryId(int categoryId, Pageable pageable);

    @Modifying
    @Transactional
    @Query("UPDATE Product p SET p.views = p.views + 1 WHERE p.id = :productId")
    void viewProduct(@Param("productId") int productId);

    @Query("SELECT p FROM Product p ORDER BY p.views DESC LIMIT :limit")
    List<Product> getHotProducts(@Param("limit") int limit);

    @Query(value = """
    SELECT p.* 
    FROM products p
    JOIN order_details od ON od.product_id = p.id
    JOIN orders o ON o.id = od.order_id
    WHERE MONTH(o.created_at) = :month AND YEAR(o.created_at) = :year
    GROUP BY p.id, p.name
    ORDER BY SUM(od.quantity) DESC
    LIMIT 10
    """, nativeQuery = true)
    List<Product> getTop10HotestProduct(@Param("month") int month, @Param("year") int year);

}

package com.github.rinnn31.shoppydex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;
import com.github.rinnn31.shoppydex.model.Product;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Modifying
    @Transactional
    @Query("DELETE FROM Product p WHERE p.category.categoryId = :categoryId")
    void deleteByCategoryId( Long categoryId);

    @Query("SELECT COALESCE(SUM(p.stock), 0) FROM Product p WHERE p.category.categoryId = :categoryId")
    int sumStockByCategoryId( Long categoryId);

    @Query("SELECT p.category.categoryId FROM Product p WHERE p.productId = :productId")
    Long findCategoryIdByProductId( Long productId);

    Optional<Product> findByName(String name);
    boolean existsByName(String name);
}


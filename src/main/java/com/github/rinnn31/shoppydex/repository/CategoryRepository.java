package com.github.rinnn31.shoppydex.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import org.springframework.stereotype.Repository;

import org.springframework.transaction.annotation.Transactional;

import com.github.rinnn31.shoppydex.model.Category;


@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Modifying
    @Transactional
    @Query("UPDATE Category c SET c.stock = :totalStock WHERE c.categoryId = :categoryId")
    void updateStock(@Param("categoryId") Long categoryId, @Param("totalStock") int totalStock);
    public boolean existsByName(String name);
    public List<Category> findByType(String type);
    @Query("SELECT DISTINCT c.type FROM Category c")
    List<String> findDistinctByType();
}

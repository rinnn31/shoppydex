package com.github.rinnn31.shoppydex.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.github.rinnn31.shoppydex.model.entity.ProductEntity;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
    public boolean existsByName(String name);

    public List<ProductEntity> findByCategory(String category);

    public List<ProductEntity> findByCategoryIn(List<String> categories);

    public Page<ProductEntity> findByCategoryIn(List<String> categories, Pageable pageable);

    @Query("SELECT DISTINCT category FROM Product")
    List<String> findDistinctCategories();
}

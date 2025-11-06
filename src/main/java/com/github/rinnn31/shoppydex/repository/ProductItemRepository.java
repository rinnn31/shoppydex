package com.github.rinnn31.shoppydex.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.rinnn31.shoppydex.model.entity.ProductItemEntity;

@Repository
public interface ProductItemRepository extends JpaRepository<ProductItemEntity, Long> {
    void deleteByProduct_ProductId(Long productId);

    void deleteTopNByProduct_ProductId(Long productId, int n);

    List<ProductItemEntity> findTopNByProduct_ProductId(Long productId, int n);

    List<ProductItemEntity> findAllByProduct_ProductId(Long productId);
}


package com.github.rinnn31.shoppydex.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.rinnn31.shoppydex.model.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
}

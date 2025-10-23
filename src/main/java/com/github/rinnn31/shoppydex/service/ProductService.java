package com.github.rinnn31.shoppydex.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.rinnn31.shoppydex.repository.CategoryRepository;
import com.github.rinnn31.shoppydex.repository.ProductRepository;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

}

package com.github.rinnn31.shoppydex.model.dto;

import java.util.List;

import org.springframework.data.domain.Page;

public class ProductsPageModel {
    private List<ProductModel> products;

    private int currentPage;

    private long totalItems;

    private int totalPages;

    public ProductsPageModel(Page<ProductModel> productPage) {
        this.products = productPage.getContent();
        this.currentPage = productPage.getNumber();
        this.totalItems = productPage.getTotalElements();
        this.totalPages = productPage.getTotalPages();
    }

    public List<ProductModel> getProducts() {
        return products;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public long getTotalItems() {
        return totalItems;
    }

    public int getTotalPages() {
        return totalPages;
    }    
}

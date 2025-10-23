package com.github.rinnn31.shoppydex.model.api;

import java.util.List;
import java.util.Map;

import com.github.rinnn31.shoppydex.model.Product;

public class ProductDTO {
    private final Long productId;

    private final String name;

    private final String description;

    private final Double price;

    private final Map<String, Object> extras;

    private final Long categoryId;

    private final List<String> images;

    public ProductDTO(Product product) {
        this.productId = product.getProductId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.categoryId = product.getCategory().getProductId();
        this.images = List.of(product.getImages().split(","));
        this.extras = product.getExtras();
    }

    public Long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Double getPrice() {
        return price;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public List<String> getProductImages() {
        return images;
    }

    public Map<String, Object> getExtras() {
        return extras;
    }
}

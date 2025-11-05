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

    // thêm các field mới cho request
    private String value;
    private Integer stock;

    public ProductDTO() {
        this.productId = null;
        this.name = null;
        this.description = null;
        this.price = null;
        this.extras = null;
        this.categoryId = null;
        this.images = null;
    }

    public ProductDTO(Product product) {
        this.productId = product.getProductId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.categoryId = product.getCategory().getCategoryId();
        this.images = List.of(product.getImages().split(","));
        this.extras = product.getExtras();
        this.value = product.getValue();
        this.stock = product.getStock();
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

    public String getValue() {
        return value;
    }

    public Integer getStock() {
        return stock;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }
}

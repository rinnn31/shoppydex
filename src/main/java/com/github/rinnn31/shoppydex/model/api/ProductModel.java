package com.github.rinnn31.shoppydex.model.api;

import java.util.List;

import com.github.rinnn31.shoppydex.model.entity.ProductEntity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

public class ProductModel {
    private Long productId;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    private  String name;

    private String description;
    
    @NotBlank(message = "Danh mục không được để trống")
    private String category;

    @NotBlank(message = "Loại sản phẩm không được để trống")
    @Pattern(regexp="^(UNIQUE_ITEM|MULTI_ITEM)$", message="Loại sản phẩm phải là UNIQUE_ITEM hoặc MULTI_ITEM")
    private String productType;

    @PositiveOrZero(message = "Giá sản phẩm phải không được âm")
    private Double price;

    private Integer stock;

    private List<String> images;
    
    public ProductModel(){}

    public ProductModel(ProductEntity entity) {
        this.productId = entity.getProductId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.price = entity.getPrice();
        this.stock = entity.getStock();
        this.category = entity.getCategory();
        this.productType = entity.getProductType();
        this.images = entity.getImages();
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
    public String getCategory() {
        return category;
    }
    public Integer getStock() {
        return stock;
    }

    public String getProductType() {
        return productType;
    }

    public List<String> getImages() {
        return images;
    }

    public void setCategoryId(Long categoryId) {
        this.productId = categoryId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public void setCategory(String type) {
        this.category = type;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }
    
    public void setImages(List<String> images) {
        this.images = images;
    }
}

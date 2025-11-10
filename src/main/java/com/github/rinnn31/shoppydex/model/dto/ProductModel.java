package com.github.rinnn31.shoppydex.model.dto;

import java.time.LocalDateTime;

import com.github.rinnn31.shoppydex.model.entity.ProductEntity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

public class ProductModel {
    private Long productId;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    private  String name;

    private String description;
    
    @NotBlank(message = "Danh mục không được để trống")
    private String category;

    @PositiveOrZero(message = "Giá sản phẩm phải không được âm")
    private Integer price;

    private Integer stock;

    private String image;
    
    private LocalDateTime createAt;
    
    public ProductModel(){}

    public ProductModel(ProductEntity entity) {
        this.productId = entity.getProductId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.price = entity.getPrice();
        this.stock = entity.getStock();
        this.category = entity.getCategory();
        this.image = entity.getImage();
        this.createAt = entity.getCreateAt();
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

    public Integer getPrice() {
        return price;
    }
    public String getCategory() {
        return category;
    }
    public Integer getStock() {
        return stock;
    }

    public String getImage() {
        return image;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public void setCategory(String type) {
        this.category = type;
    }
    
    public void setImage(String image) {
        this.image = image;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }
}

package com.github.rinnn31.shoppydex.model.api;

import com.github.rinnn31.shoppydex.model.Category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.PositiveOrZero;

public class CategoryDTO {
    private Long categoryId;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    private  String name;

    private String description;
    private String type;
    @NotBlank(message = "Loại sản phẩm không được để trống")
    @Pattern(regexp="^(UNIQUE_ITEM|MULTI_ITEM)$", message="Loại sản phẩm phải là UNIQUE_ITEM hoặc MULTI_ITEM")
    private String productType;
    @PositiveOrZero(message = "Giá sản phẩm phải không được âm")
    private Double price;

    private Integer stock = 0;

    private String categoryImage;
    
    public CategoryDTO(){}
    public CategoryDTO(Category category) {
        this.categoryId = category.getProductId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.price = category.getPrice();
        this.stock = category.getStock();
        this.type = category.getType();
        this.productType = category.getProductType();
        this.categoryImage = category.getCategoryImage();
    }

    public Long getCategoryId() {
        return categoryId;
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
    public String getType() {
        return type;
    }
    public Integer getStock() {
        return stock;
    }

    public String getProductType() {
        return productType;
    }

    public String getCategoryImage() {
        return categoryImage;
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
    public void setType(String type) {
        this.type = type;
    }
    public void setProductType(String productType) {
        this.productType = productType;
    }
    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }
}

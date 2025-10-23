package com.github.rinnn31.shoppydex.model.api;

import com.github.rinnn31.shoppydex.model.Category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CategoryDTO {
    private final long categoryId;

    @NotBlank(message = "Tên sản phẩm không được để trống")
    private final String name;

    private final String description;

    @NotBlank(message = "Loại sản phẩm không được để trống")
    @Pattern(regexp="^(UNIQUE_ITEM|MULTI_ITEM)$", message="Loại sản phẩm phải là UNIQUE_ITEM hoặc MULTI_ITEM")
    private final String productType;

    private final Double price;

    private final Integer stock;

    private final String categoryImage;

    public CategoryDTO(Category category) {
        this.categoryId = category.getProductId();
        this.name = category.getName();
        this.description = category.getDescription();
        this.price = category.getPrice();
        this.stock = category.getStock();
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

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public String getProductType() {
        return productType;
    }

    public String getCategoryImage() {
        return categoryImage;
    }
}

package com.github.rinnn31.shoppydex.model.api;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.rinnn31.shoppydex.model.entity.ProductItemEntity;

import jakarta.validation.constraints.NotBlank;

public class ProductItemModel {
    private Long productItemId;

    private String name;

    @NotBlank(message = "Thông tin sản phẩm không được để trống")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String value;

    private String description;

    private Double price;

    private Map<String, Object> extras;

    private Long productId;

    private List<String> images;

    public ProductItemModel() {
        
    }


    public ProductItemModel(ProductItemEntity entity) {
        this.productItemId = entity.getProductItemId();
        this.name = entity.getName();
        this.description = entity.getDescription();
        this.value = entity.getValue();
        this.price = entity.getPrice();
        this.productId = entity.getProduct().getProductId();
        this.images = entity.getImages();
        this.extras = entity.getExtras();
    }

    public Long getProductId() {
        return productItemId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getValue() {
        return value;
    }

    public Double getPrice() {
        return price;
    }

    public Long getCategoryId() {
        return productId;
    }

    public List<String> getImages() {
        return images;
    }

    public Map<String, Object> getExtras() {
        return extras;
    }

    public Long getProductItemId() {
        return productItemId;
    }

    public void setProductItemId(Long productItemId) {
        this.productItemId = productItemId;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setExtras(Map<String, Object> extras) {
        this.extras = extras;
    }
}

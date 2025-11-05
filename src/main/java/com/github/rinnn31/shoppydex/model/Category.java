package com.github.rinnn31.shoppydex.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Category {
    public static final String TYPE_UNIQUE_ITEMS = "UNIQUE_ITEM";
    public static final String TYPE_MULTI_ITEMS = "MULTI_ITEM";

    @Id
    @Column(name = "CategoryID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="category_seq_gen")
    @SequenceGenerator(name="category_seq_gen", sequenceName="category_seq", allocationSize=1, initialValue=1000000)
    private Long categoryId;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Description", nullable = true)
    private String description;

    @Column(name = "Price")
    private Double price;

    @Column(name = "Stock", nullable = false)
    private Integer stock;

    @Column(name = "Type", nullable = false)
    private String type;

    @Column(name = "ProductType", nullable = false)
    private String productType;

    @Column(name = "CategoryImage", nullable = true)
    private String categoryImage;

    public Category() {
        // Default constructor for JPA
    }

    public Category(String name, String description, double price, String type, String productType) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.type = type;
        this.productType = productType;
        this.stock = 0;
        this.categoryImage = null;
    }

    public Long getProductId() {
        return categoryId;
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

    public Integer getStock() {
        return stock;
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

    public String getCategory() {
        return type;
    }

    public void setCategory(String category) {
        this.type = category;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productItemType) {
        if (TYPE_UNIQUE_ITEMS.equals(productItemType) || TYPE_MULTI_ITEMS.equals(productItemType)) {
            this.productType = productItemType;
        } else {
            throw new IllegalArgumentException("Invalid product type: " + productItemType);
        }
    }

    public String getCategoryImage() {
        return categoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        this.categoryImage = categoryImage;
    }

    public String getType() {
       return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}

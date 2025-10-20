package com.github.rinnn31.shoppydex.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false, unique = true)
    private final Long mProductId;

    @Column(name = "name", nullable = false)
    private String mName;

    @Column(name = "description", nullable = true)
    private String mDescription;

    @Column(name = "price", nullable = false)
    private double mPrice;

    @Column(name = "stock", nullable = false)
    private int mStock;

    public Product(Long productId, String name, String description, double price, int stock) {
        mProductId = productId;
        mName = name;
        mDescription = description;
        mPrice = price;
        mStock = stock;
    }

    public Long getProductId() {
        return mProductId;
    }

    public String getName() {
        return mName;
    }

    public String getDescription() {
        return mDescription;
    }

    public double getPrice() {
        return mPrice;
    }

    public int getStock() {
        return mStock;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setDescription(String description) {
        mDescription = description;
    }

    public void setPrice(double price) {
        mPrice = price;
    }

    public void setStock(int stock) {
        mStock = stock;
    }
}

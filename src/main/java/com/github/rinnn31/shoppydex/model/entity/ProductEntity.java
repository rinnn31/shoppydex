package com.github.rinnn31.shoppydex.model.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.SequenceGenerator;

@Entity(name = "Product")
public class ProductEntity {
    @Id
    @Column(name = "ProductId")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="category_seq_gen")
    @SequenceGenerator(name="category_seq_gen", sequenceName="category_seq", allocationSize=1, initialValue=1000000)
    private long productId;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Description")
    @Lob
    private String description;

    @Column(name = "Price", nullable = false)
    private int price;

    @Column(name = "Stock", nullable = false)
    private int stock;

    @Column(name = "Category", nullable = false)
    private String category;

    // Lưu trữ danh sách đường dẫn hình ảnh, cách nhau bằng dấu phẩy
    @Column(name = "Image")
    private String image;

    @Column(name = "CreatedAt", nullable = false)
    private LocalDateTime createAt;

    public ProductEntity() {
        // Default constructor for JPA
    }

    public ProductEntity(String name, String category, int price) {
        this.name = name;
        this.description = null;
        this.price = price;
        this.category = category;
        this.stock = 0;
        this.image = null;
        this.createAt = LocalDateTime.now();
    }

    public long getProductId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getImage() {
        return image;
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

package com.github.rinnn31.shoppydex.model;

import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE, generator="product_seq_gen")
    @SequenceGenerator(name="product_seq_gen", sequenceName="product_seq", allocationSize=50, initialValue=1000000)
    @Column(name = "ProductID")
    private Long productId;

    @Column(name = "Name", nullable = false)
    private String name;

    @Column(name = "Description")
    private String description;

    @Column(name = "Value", nullable = false)
    private String value;

    @Column(name = "Price")
    private Double price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CategoryID", referencedColumnName = "Category", nullable = false)
    private Category category;

    @Column(name = "Extras", nullable = true, columnDefinition="JSON")
    @JdbcTypeCode(SqlTypes.JSON)
    private Map<String, Object> extras;

    @Column(name = "Images", nullable = true)
    private String images;

    public Product() {
        // Default constructor for JPA
    }

    public Product(String name, String value,  Category category) {
        this.name = name;
        this.value = value;
        this.category = category;
    }

    public Long getProductId() {
        return productId;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category product) {
        this.category = product;
    }

    public Map<String, Object> getExtras() {
        return extras;
    }

    public void setExtras(Map<String, Object> extras) {
        this.extras = extras;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

}

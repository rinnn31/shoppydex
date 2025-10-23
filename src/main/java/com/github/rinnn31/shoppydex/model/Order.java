package com.github.rinnn31.shoppydex.model;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

@Entity
public class Order {
    @Id
    @Column(name= "OrderId", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "Owner", nullable = false, referencedColumnName = "UserID")
    private User owner;

    @ManyToOne
    @JoinColumn(name = "Product", nullable = false, referencedColumnName = "ProductID")
    private Category product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "TotalPrice", nullable = false)
    private double totalPrice;

    @Column(name = "OrderDate", nullable = false)
    private Date orderDate;

    public Order() {
        // Default constructor for JPA
    }

    public Order(Long orderId, User owner, Category product, int quantity, double totalPrice, Date orderDate) {
        this.orderId = orderId;
        this.owner = owner;
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public User getOwner() {
        return owner;
    }

    public void setOwner(User owner) {
        this.owner = owner;
    }

    public Category getProduct() {
        return product;
    }

    public void setProduct(Category product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

}

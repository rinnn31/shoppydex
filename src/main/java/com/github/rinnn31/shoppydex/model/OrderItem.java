package com.github.rinnn31.shoppydex.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_items")
public class OrderItem {
    @Id
    @Column(name= "order_id", nullable = false, unique = true)
    private Long orderId;

    @ManyToOne
    @JoinColumn(name = "owner", nullable = false, referencedColumnName = "id")
    private Account owner;

    @ManyToOne
    @JoinColumn(name = "product", nullable = false)
    private Product product;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    public OrderItem(Long orderId, Account owner, Product product, int quantity) {
        this.orderId = orderId;
        this.owner = owner;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Account getOwner() {
        return owner;
    }

    public void setOwner(Account owner) {
        this.owner = owner;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


}

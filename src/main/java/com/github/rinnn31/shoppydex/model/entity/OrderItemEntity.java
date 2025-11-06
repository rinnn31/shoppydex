package com.github.rinnn31.shoppydex.model.entity;

import java.util.Date;
import java.util.List;

import com.github.rinnn31.shoppydex.utils.StringArrayConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

@Entity
@Table(name = "OrderItem")
public class OrderItemEntity {
    @Id
    @Column(name= "OrderId", nullable = false, unique = true)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator="order_seq_gen")
    @SequenceGenerator(name="order_seq_gen", sequenceName="order_seq", allocationSize=1, initialValue=5000000)
    private Long orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserId", nullable = false, referencedColumnName = "UserID")
    private UserEntity user;

    @Lob
    @Column(name = "ProductValues", nullable = false)
    @Convert(converter = StringArrayConverter.class)
    private List<String> values;

    @Column(name = "Description", nullable = false)
    private String description;

    @Column(name = "TotalPrice", nullable = false)
    private Double totalPrice;

    @Column(name = "OrderDate", nullable = false)
    private Date orderDate;

    public OrderItemEntity() {
        // Default constructor for JPA
    }

    public OrderItemEntity(Long orderId, UserEntity owner, List<String> values, String description, Integer quantity, double totalPrice, Date orderDate) {
        this.orderId = orderId;
        this.user = owner;
        this.values = values;   
        this.description = description;
        this.totalPrice = totalPrice;
        this.orderDate = orderDate;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity owner) {
        this.user = owner;
    }

    public List<String> getValues() {
        return values;
    }

    public void setValues(List<String> values) {
        this.values = values;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
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

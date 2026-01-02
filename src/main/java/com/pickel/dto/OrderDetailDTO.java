package com.pickel.dto;

import com.pickel.entity.Order;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class OrderDetailDTO {
    private Long orderId;
    private String username;
    private String phoneNumber;
    private LocalDateTime createdAt;
    private String deliveryAddress;
    private String notes;
    private Order.OrderStatus status;
    private BigDecimal totalAmount;
    private String itemName;
    private Integer quantity;
    private BigDecimal itemPrice;
    private BigDecimal subtotal;
    
    // LOCATION FIELDS
    private String mapLink;
    private String deliveryCity;
    private String deliveryPincode;

    // Constructor for JPA projection - MUST MATCH QUERY ORDER
    public OrderDetailDTO(Long orderId, String username, String phoneNumber, 
                         LocalDateTime createdAt, String deliveryAddress, String notes,
                         Order.OrderStatus status, BigDecimal totalAmount, String itemName,
                         Integer quantity, BigDecimal itemPrice, BigDecimal subtotal,
                         String mapLink, String deliveryCity, String deliveryPincode) {
        this.orderId = orderId;
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.createdAt = createdAt;
        this.deliveryAddress = deliveryAddress;
        this.notes = notes;
        this.status = status;
        this.totalAmount = totalAmount;
        this.itemName = itemName;
        this.quantity = quantity;
        this.itemPrice = itemPrice;
        this.subtotal = subtotal;
        this.mapLink = mapLink;
        this.deliveryCity = deliveryCity;
        this.deliveryPincode = deliveryPincode;
    }

    // Getters and Setters
    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Order.OrderStatus getStatus() {
        return status;
    }

    public void setStatus(Order.OrderStatus status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(BigDecimal itemPrice) {
        this.itemPrice = itemPrice;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }

    public String getMapLink() {
        return mapLink;
    }

    public void setMapLink(String mapLink) {
        this.mapLink = mapLink;
    }

    public String getDeliveryCity() {
        return deliveryCity;
    }

    public void setDeliveryCity(String deliveryCity) {
        this.deliveryCity = deliveryCity;
    }

    public String getDeliveryPincode() {
        return deliveryPincode;
    }

    public void setDeliveryPincode(String deliveryPincode) {
        this.deliveryPincode = deliveryPincode;
    }
}

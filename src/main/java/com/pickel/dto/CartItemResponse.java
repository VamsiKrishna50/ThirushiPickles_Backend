package com.pickel.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;


class CartItemResponse {
    
    private Long id;
    private Long pickleId;
    private String pickleName;
    private String pickleDescription;
    private String pickleImageUrl;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;

    public CartItemResponse() {
    }

    public CartItemResponse(Long id, Long pickleId, String pickleName, String pickleDescription, 
                           String pickleImageUrl, BigDecimal price, Integer quantity, BigDecimal subtotal) {
        this.id = id;
        this.pickleId = pickleId;
        this.pickleName = pickleName;
        this.pickleDescription = pickleDescription;
        this.pickleImageUrl = pickleImageUrl;
        this.price = price;
        this.quantity = quantity;
        this.subtotal = subtotal;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPickleId() {
        return pickleId;
    }

    public void setPickleId(Long pickleId) {
        this.pickleId = pickleId;
    }

    public String getPickleName() {
        return pickleName;
    }

    public void setPickleName(String pickleName) {
        this.pickleName = pickleName;
    }

    public String getPickleDescription() {
        return pickleDescription;
    }

    public void setPickleDescription(String pickleDescription) {
        this.pickleDescription = pickleDescription;
    }

    public String getPickleImageUrl() {
        return pickleImageUrl;
    }

    public void setPickleImageUrl(String pickleImageUrl) {
        this.pickleImageUrl = pickleImageUrl;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(BigDecimal subtotal) {
        this.subtotal = subtotal;
    }
}
package com.pickel.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;


class CartResponse {
    
    private Long id;
    private List<CartItemResponse> items;
    private Integer totalItems;
    private BigDecimal totalPrice;

    public CartResponse() {
    }

    public CartResponse(Long id, List<CartItemResponse> items, Integer totalItems, BigDecimal totalPrice) {
        this.id = id;
        this.items = items;
        this.totalItems = totalItems;
        this.totalPrice = totalPrice;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<CartItemResponse> getItems() {
        return items;
    }

    public void setItems(List<CartItemResponse> items) {
        this.items = items;
    }

    public Integer getTotalItems() {
        return totalItems;
    }

    public void setTotalItems(Integer totalItems) {
        this.totalItems = totalItems;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
}
package com.pickel.dto;

import com.pickel.entity.Pickle.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;

public class PickleRequest {
    @NotBlank(message = "Name is required")
    private String name;

    private String description;
    
    @Positive(message = "Base weight must be positive")
    private Integer baseWeight = 250; // Base weight in grams (default 250g)
    
    @Positive(message = "Price must be positive")
    private BigDecimal price; // Price for the base weight

    private Integer stockQuantity;
    private String imageUrl;
    private Category category;

    public PickleRequest() {
        super();
    }

    public PickleRequest(@NotBlank(message = "Name is required") String name, 
                         String description, Integer baseWeight,
                         @Positive(message = "Price must be positive") BigDecimal price, 
                         Integer stockQuantity, String imageUrl, Category category) {
        super();
        this.name = name;
        this.description = description;
        this.baseWeight = baseWeight;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getBaseWeight() {
        return baseWeight;
    }

    public void setBaseWeight(Integer baseWeight) {
        this.baseWeight = baseWeight;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(Integer stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }
}
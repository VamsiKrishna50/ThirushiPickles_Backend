package com.pickel.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "pickles")
public class Pickle {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Pickle name is required")
    @Column(nullable = false)
    private String name;
    
    @Column(length = 1000)
    private String description;
    
    @Positive(message = "Price must be positive")
    @Column(nullable = false)
    private BigDecimal price; // Base price for baseWeight (e.g., 250g)
    
    @Column(nullable = false)
    private Integer stockQuantity;
    
    @Column(nullable = false)
    private Integer baseWeight = 250; // Base weight in grams (default 250g)
    
    private String imageUrl;
    
    @Enumerated(EnumType.STRING)
    private Category category;
    
    @Column(nullable = false)
    private Boolean available = true;
    
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
    
    // Method to calculate price based on selected weight
    public BigDecimal calculatePrice(Integer selectedWeight) {
        if (selectedWeight == null || baseWeight == null || baseWeight == 0) {
            return price;
        }
        BigDecimal multiplier = new BigDecimal(selectedWeight).divide(new BigDecimal(baseWeight), 2, BigDecimal.ROUND_HALF_UP);
        return price.multiply(multiplier).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
    
    public enum Category {
        VEG, NON_VEG, MIXED
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    public Integer getBaseWeight() {
        return baseWeight;
    }

    public void setBaseWeight(Integer baseWeight) {
        this.baseWeight = baseWeight;
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

    public Boolean getAvailable() {
        return available;
    }

    public void setAvailable(Boolean available) {
        this.available = available;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Pickle() {
        super();
    }

    public Pickle(Long id, String name, String description, BigDecimal price, 
                  Integer stockQuantity, Integer baseWeight, String imageUrl,
                  Category category, Boolean available, LocalDateTime createdAt, 
                  LocalDateTime updatedAt) {
        super();
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.baseWeight = baseWeight;
        this.imageUrl = imageUrl;
        this.category = category;
        this.available = available;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
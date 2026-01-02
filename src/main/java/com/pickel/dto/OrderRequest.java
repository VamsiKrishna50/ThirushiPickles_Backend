package com.pickel.dto;

import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class OrderRequest {
    @NotEmpty(message = "Order items cannot be empty")
    private List<OrderItemRequest> items;
    
    private String deliveryAddress;
    private String notes;
    
    // NEW LOCATION FIELDS
    private Double deliveryLatitude;
    private Double deliveryLongitude;
    private String mapLink;
    private String deliveryCity;
    private String deliveryPincode;
    private String customerName;
    private String customerPhone;
    
    // Existing getters and setters
    public List<OrderItemRequest> getItems() {
        return items;
    }
    
    public void setItems(List<OrderItemRequest> items) {
        this.items = items;
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
    
    // NEW GETTERS AND SETTERS
    public Double getDeliveryLatitude() {
        return deliveryLatitude;
    }
    
    public void setDeliveryLatitude(Double deliveryLatitude) {
        this.deliveryLatitude = deliveryLatitude;
    }
    
    public Double getDeliveryLongitude() {
        return deliveryLongitude;
    }
    
    public void setDeliveryLongitude(Double deliveryLongitude) {
        this.deliveryLongitude = deliveryLongitude;
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
    
    public String getCustomerName() {
        return customerName;
    }
    
    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }
    
    public String getCustomerPhone() {
        return customerPhone;
    }
    
    public void setCustomerPhone(String customerPhone) {
        this.customerPhone = customerPhone;
    }
    
    public OrderRequest() {
        super();
    }
    
    public static class OrderItemRequest {
        private Long pickleId;
        private Integer quantity;
        
        public Long getPickleId() {
            return pickleId;
        }
        
        public void setPickleId(Long pickleId) {
            this.pickleId = pickleId;
        }
        
        public Integer getQuantity() {
            return quantity;
        }
        
        public void setQuantity(Integer quantity) {
            this.quantity = quantity;
        }
        
        public OrderItemRequest(Long pickleId, Integer quantity) {
            super();
            this.pickleId = pickleId;
            this.quantity = quantity;
        }
        
        public OrderItemRequest() {
            super();
        }
    }
}
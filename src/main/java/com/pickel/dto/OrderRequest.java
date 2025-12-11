package com.pickel.dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;


public class OrderRequest {
    @NotEmpty(message = "Order items cannot be empty")
    private List<OrderItemRequest> items;
    
    private String deliveryAddress;
    private String notes;
    
    
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
        
    }
    
}
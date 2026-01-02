package com.pickel.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;
import java.util.List;

public class AddToCartRequest {
    
    @NotNull(message = "Pickle ID is required")
    private Long pickleId;
    
    @Positive(message = "Quantity must be positive")
    private Integer quantity = 1;
    private Integer selectedWeight; // 250, 500, or 1000
    public AddToCartRequest() {
    }

    

    public AddToCartRequest(@NotNull(message = "Pickle ID is required") Long pickleId,
			@Positive(message = "Quantity must be positive") Integer quantity, Integer selectedWeight) {
		super();
		this.pickleId = pickleId;
		this.quantity = quantity;
		this.selectedWeight = selectedWeight;
	}



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



	public Integer getSelectedWeight() {
		return selectedWeight;
	}



	public void setSelectedWeight(Integer selectedWeight) {
		this.selectedWeight = selectedWeight;
	}
    
}

package com.pickel.dto;

import lombok.AllArgsConstructor;
import lombok.Data;


public class ContactResponse {
    private boolean success;
    private String message;
	public boolean isSuccess() {
		return success;
	}
	public void setSuccess(boolean success) {
		this.success = success;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ContactResponse(boolean success, String message) {
		super();
		this.success = success;
		this.message = message;
	}
	public ContactResponse() {
		super();
		// TODO Auto-generated constructor stub
	}
    
}

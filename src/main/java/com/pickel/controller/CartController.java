package com.pickel.controller;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.pickel.dto.AddToCartRequest;
import com.pickel.entity.Cart;
import com.pickel.entity.CartItem;
import com.pickel.service.CartService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/customer/cart")
@CrossOrigin(origins = "http://localhost:3000")
@PreAuthorize("hasAuthority('CUSTOMER')")
public class CartController {
    
    @Autowired
    private CartService cartService;
    
    @GetMapping
    public ResponseEntity<Map<String, Object>> getCart(Authentication authentication) {
        Cart cart = cartService.getCart(authentication.getName());
        return ResponseEntity.ok(buildCartResponse(cart));
    }
    
    @PostMapping
    public ResponseEntity<Map<String, Object>> addToCart(
            @Valid @RequestBody AddToCartRequest request,
            Authentication authentication) {
        
        System.out.println("=== ADD TO CART REQUEST ===");
        System.out.println("Pickle ID: " + request.getPickleId());
        System.out.println("Quantity: " + request.getQuantity());
        System.out.println("Selected Weight: " + request.getSelectedWeight());
        
        Cart cart = cartService.addToCart(authentication.getName(), request);
        Map<String, Object> response = buildCartResponse(cart);
        
        System.out.println("=== CART RESPONSE ===");
        System.out.println("Total Items: " + response.get("totalItems"));
        System.out.println("Total Price: " + response.get("totalPrice"));
        
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @PutMapping("/{cartItemId}")
    public ResponseEntity<Map<String, Object>> updateCartItem(
            @PathVariable Long cartItemId,
            @RequestBody Map<String, Integer> payload,
            Authentication authentication) {
        
        Integer quantity = payload.get("quantity");
        if (quantity == null || quantity < 0) {
            return ResponseEntity.badRequest()
                    .body(Map.of("error", "Invalid quantity"));
        }
        
        Cart cart = cartService.updateCartItem(authentication.getName(), cartItemId, quantity);
        return ResponseEntity.ok(buildCartResponse(cart));
    }
    
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Map<String, Object>> removeFromCart(
            @PathVariable Long cartItemId,
            Authentication authentication) {
        Cart cart = cartService.removeFromCart(authentication.getName(), cartItemId);
        return ResponseEntity.ok(buildCartResponse(cart));
    }
    
    @DeleteMapping
    public ResponseEntity<Map<String, String>> clearCart(Authentication authentication) {
        cartService.clearCart(authentication.getName());
        return ResponseEntity.ok(Map.of("message", "Cart cleared successfully"));
    }
    
    private Map<String, Object> buildCartResponse(Cart cart) {
        List<Map<String, Object>> items = cart.getItems().stream()
                .map(this::buildCartItemResponse)
                .collect(Collectors.toList());
        
        int totalItems = cart.getItems().stream()
                .mapToInt(CartItem::getQuantity)
                .sum();
        
        BigDecimal totalPrice = cart.getItems().stream()
                .map(item -> item.getPriceAtAddition()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        Map<String, Object> response = new HashMap<>();
        response.put("id", cart.getId());
        response.put("items", items);
        response.put("totalItems", totalItems);
        response.put("totalPrice", totalPrice);
        
        return response;
    }
    
    private Map<String, Object> buildCartItemResponse(CartItem item) {
        Map<String, Object> itemMap = new HashMap<>();
        
        // CRITICAL FIX: Use priceAtAddition (the calculated price based on weight)
        // NOT the pickle's base price
        BigDecimal itemPrice = item.getPriceAtAddition();
        BigDecimal subtotal = itemPrice.multiply(BigDecimal.valueOf(item.getQuantity()));
        
        itemMap.put("id", item.getId());
        itemMap.put("pickleId", item.getPickle().getId());
        itemMap.put("pickleName", item.getPickle().getName());
        itemMap.put("pickleDescription", item.getPickle().getDescription());
        itemMap.put("pickleImageUrl", item.getPickle().getImageUrl());
        itemMap.put("price", itemPrice); // This is the price for the selected weight
        itemMap.put("quantity", item.getQuantity());
        itemMap.put("selectedWeight", item.getSelectedWeight());
        itemMap.put("subtotal", subtotal);
        
        // Add pickle object for frontend compatibility
        // Use the CALCULATED price, not base price
        Map<String, Object> pickle = new HashMap<>();
        pickle.put("id", item.getPickle().getId());
        pickle.put("name", item.getPickle().getName());
        pickle.put("price", itemPrice); // IMPORTANT: Use calculated price
        pickle.put("imageUrl", item.getPickle().getImageUrl());
        pickle.put("selectedWeight", item.getSelectedWeight());
        itemMap.put("pickle", pickle);
        
        System.out.println("Cart Item Response - Name: " + item.getPickle().getName() + 
                         ", Weight: " + item.getSelectedWeight() + 
                         "g, Price: " + itemPrice + 
                         ", Quantity: " + item.getQuantity() + 
                         ", Subtotal: " + subtotal);
        
        return itemMap;
    }
}
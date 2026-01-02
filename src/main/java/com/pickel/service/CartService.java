package com.pickel.service;

import com.pickel.dto.AddToCartRequest;
import com.pickel.entity.Cart;
import com.pickel.entity.CartItem;
import com.pickel.entity.Pickle;
import com.pickel.entity.User;
import com.pickel.exception.ResourceNotFoundException;
import com.pickel.repository.CartItemRepository;
import com.pickel.repository.CartRepository;
import com.pickel.repository.PickleRepository;
import com.pickel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CartService {
    
    @Autowired
    private CartRepository cartRepository;
    
    @Autowired
    private CartItemRepository cartItemRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PickleRepository pickleRepository;
    
    @Transactional
    public Cart getOrCreateCart(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Optional<Cart> existingCart = cartRepository.findByUserWithItems(user);
        
        if (existingCart.isPresent()) {
            Cart cart = existingCart.get();
            cart.getItems().size();
            return cart;
        }
        
        Cart newCart = new Cart();
        newCart.setUser(user);
        return cartRepository.save(newCart);
    }
    
    @Transactional
    public Cart addToCart(String username, AddToCartRequest request) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Pickle pickle = pickleRepository.findById(request.getPickleId())
                .orElseThrow(() -> new ResourceNotFoundException("Pickle not found"));
        
        if (pickle.getStockQuantity() < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock");
        }
        
        // Set default weight if not provided
        Integer selectedWeight = request.getSelectedWeight() != null ? request.getSelectedWeight() : 250;
        
        // Calculate price based on selected weight
        BigDecimal priceForWeight = calculatePriceForWeight(pickle.getPrice(), selectedWeight);
        
        Cart cart = getOrCreateCart(username);
        
        // Check if same pickle with same weight exists
        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getPickle().getId().equals(pickle.getId()) 
                        && item.getSelectedWeight().equals(selectedWeight))
                .findFirst();
        
        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            int newQuantity = item.getQuantity() + request.getQuantity();
            
            if (pickle.getStockQuantity() < newQuantity) {
                throw new RuntimeException("Insufficient stock");
            }
            
            item.setQuantity(newQuantity);
            cartItemRepository.save(item);
        } else {
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setPickle(pickle);
            newItem.setQuantity(request.getQuantity());
            newItem.setSelectedWeight(selectedWeight);
            newItem.setPriceAtAddition(priceForWeight);
            cartItemRepository.save(newItem);
            cart.getItems().add(newItem);
        }
        
        return cartRepository.save(cart);
    }
    
    private BigDecimal calculatePriceForWeight(BigDecimal basePrice, Integer weight) {
        if (weight == null) {
            weight = 250;
        }
        
        BigDecimal multiplier;
        switch (weight) {
            case 250:
                multiplier = BigDecimal.ONE;
                break;
            case 500:
                multiplier = BigDecimal.valueOf(1.9);
                break;
            case 1000:
                multiplier = BigDecimal.valueOf(3.6);
                break;
            default:
                multiplier = BigDecimal.ONE;
        }
        
        return basePrice.multiply(multiplier).setScale(2, BigDecimal.ROUND_HALF_UP);
    }
    
    @Transactional
    public Cart updateCartItem(String username, Long cartItemId, Integer quantity) {
        Cart cart = getOrCreateCart(username);
        
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        
        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Cart item does not belong to user");
        }
        
        if (quantity <= 0) {
            cartItemRepository.delete(cartItem);
            cart.getItems().remove(cartItem);
        } else {
            if (cartItem.getPickle().getStockQuantity() < quantity) {
                throw new RuntimeException("Insufficient stock");
            }
            cartItem.setQuantity(quantity);
            cartItemRepository.save(cartItem);
        }
        
        return cartRepository.save(cart);
    }
    
    @Transactional
    public Cart removeFromCart(String username, Long cartItemId) {
        Cart cart = getOrCreateCart(username);
        
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found"));
        
        if (!cartItem.getCart().getId().equals(cart.getId())) {
            throw new RuntimeException("Cart item does not belong to user");
        }
        
        cart.getItems().remove(cartItem);
        cartItemRepository.delete(cartItem);
        
        return cartRepository.save(cart);
    }
    
    @Transactional
    public void clearCart(String username) {
        Cart cart = getOrCreateCart(username);
        cartItemRepository.deleteByCart(cart);
        cart.getItems().clear();
        cartRepository.save(cart);
    }
    
    @Transactional(readOnly = true)
    public Cart getCart(String username) {
        return getOrCreateCart(username);
    }
}
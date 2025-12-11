package com.pickel.controller;

import com.pickel.dto.OrderRequest;
import com.pickel.entity.Order;
import com.pickel.entity.Pickle;
import com.pickel.service.OrderService;
import com.pickel.service.PickleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/customer")
@CrossOrigin(origins = "http://localhost:3000")
@PreAuthorize("hasAuthority('CUSTOMER')")
public class CustomerController {
    
    @Autowired
    private PickleService pickleService;
    
    @Autowired
    private OrderService orderService;
    
    @GetMapping("/pickles")
    public ResponseEntity<List<Pickle>> getAvailablePickles() {
        return ResponseEntity.ok(pickleService.getAvailablePickles());
    }
    
    @GetMapping("/pickles/{id}")
    public ResponseEntity<Pickle> getPickleById(@PathVariable Long id) {
        return ResponseEntity.ok(pickleService.getPickleById(id));
    }
    
    @PostMapping("/orders")
    public ResponseEntity<Order> createOrder(@Valid @RequestBody OrderRequest request, 
                                             Authentication authentication) {
        Order order = orderService.createOrder(request, authentication.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }
    
    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getMyOrders(Authentication authentication) {
        List<Order> orders = orderService.getCustomerOrders(authentication.getName());
        return ResponseEntity.ok(orders);
    }
    
    @GetMapping("/orders/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }
}
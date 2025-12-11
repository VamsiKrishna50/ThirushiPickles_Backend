package com.pickel.controller;

import com.pickel.dto.PickleRequest;
import com.pickel.entity.Order;
import com.pickel.entity.Pickle;
import com.pickel.service.OrderService;
import com.pickel.service.PickleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@CrossOrigin(origins = "http://localhost:3000")
@PreAuthorize("hasAuthority('ADMIN')")
public class AdminController {
    
    @Autowired
    private PickleService pickleService;
    
    @Autowired
    private OrderService orderService;
    
    @GetMapping("/pickles")
    public ResponseEntity<List<Pickle>> getAllPickles() {
        return ResponseEntity.ok(pickleService.getAllPickles());
    }
    
    @PostMapping("/pickles")
    public ResponseEntity<Pickle> createPickle(@Valid @RequestBody PickleRequest request) {
        Pickle pickle = pickleService.createPickle(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(pickle);
    }
    
    @PutMapping("/pickles/{id}")
    public ResponseEntity<Pickle> updatePickle(@PathVariable Long id, 
                                                @Valid @RequestBody PickleRequest request) {
        Pickle pickle = pickleService.updatePickle(id, request);
        return ResponseEntity.ok(pickle);
    }
    
    @DeleteMapping("/pickles/{id}")
    public ResponseEntity<String> deletePickle(@PathVariable Long id) {
        pickleService.deletePickle(id);
        return ResponseEntity.ok("Pickle deleted successfully");
    }
    
    @PatchMapping("/pickles/{id}/toggle-availability")
    public ResponseEntity<Pickle> togglePickleAvailability(@PathVariable Long id) {
        Pickle pickle = pickleService.toggleAvailability(id);
        return ResponseEntity.ok(pickle);
    }
    
    @GetMapping("/orders")
    public ResponseEntity<List<Order>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }
    
    @PatchMapping("/orders/{id}/status")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long id, 
                                                    @RequestParam Order.OrderStatus status) {
        Order order = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(order);
    }
}
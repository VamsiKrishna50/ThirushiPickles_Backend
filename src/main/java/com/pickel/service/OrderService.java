package com.pickel.service;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.pickel.dto.OrderDetailDTO;
import com.pickel.dto.OrderRequest;
import com.pickel.entity.Order;
import com.pickel.entity.OrderItem;
import com.pickel.entity.Pickle;
import com.pickel.entity.User;
import com.pickel.exception.ResourceNotFoundException;
import com.pickel.repository.OrderRepository;
import com.pickel.repository.PickleRepository;
import com.pickel.repository.UserRepository;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PickleRepository pickleRepository;
    
 // OrderService.java - Updated createOrder method
    @Transactional
    public Order createOrder(OrderRequest request, String username) {
        User customer = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Order order = new Order();
        order.setCustomer(customer);
        order.setDeliveryAddress(request.getDeliveryAddress());
        order.setNotes(request.getNotes());
        order.setStatus(Order.OrderStatus.PENDING);
        
        // SET LOCATION FIELDS - CRITICAL
        order.setDeliveryLatitude(request.getDeliveryLatitude());
        order.setDeliveryLongitude(request.getDeliveryLongitude());
        order.setMapLink(request.getMapLink()); // THIS MUST BE SET
        order.setDeliveryCity(request.getDeliveryCity());
        order.setDeliveryPincode(request.getDeliveryPincode());
        order.setCustomerName(request.getCustomerName());
        order.setCustomerPhone(request.getCustomerPhone());
        
        Set<OrderItem> orderItems = new HashSet<>();
        BigDecimal totalAmount = BigDecimal.ZERO;
        
        for (OrderRequest.OrderItemRequest itemRequest : request.getItems()) {
            Pickle pickle = pickleRepository.findById(itemRequest.getPickleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Pickle not found"));
            
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            orderItem.setPickle(pickle);
            orderItem.setQuantity(itemRequest.getQuantity());
            orderItem.setPrice(pickle.getPrice());
            orderItem.setSubtotal(pickle.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));
            
            orderItems.add(orderItem);
            totalAmount = totalAmount.add(orderItem.getSubtotal());
        }
        
        order.setOrderItems(orderItems);
        order.setTotalAmount(totalAmount);
        
        return orderRepository.save(order);
    }
    @Transactional(readOnly = true)  // ADDED
    public List<Order> getCustomerOrders(String username) {
        User customer = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        List<Order> orders = orderRepository.findByCustomerOrderByCreatedAtDesc(customer);
        
        // Force initialization of lazy-loaded relationships
        orders.forEach(order -> {
            order.getCustomer().getUsername(); // Initialize customer
            order.getOrderItems().forEach(item -> {
                item.getPickle().getName(); // Initialize pickle in each order item
            });
        });
        
        return orders;
    }
    
    @Transactional(readOnly = true)  // ADDED
    public Order getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
        
        // Force initialization
        order.getCustomer().getUsername();
        order.getOrderItems().forEach(item -> {
            item.getPickle().getName();
        });
        
        return order;
    }
    
    @Transactional(readOnly = true)  // ADDED
    public List<Order> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        
        // Force initialization of lazy-loaded relationships
        orders.forEach(order -> {
            order.getCustomer().getUsername(); // Initialize customer
            order.getOrderItems().forEach(item -> {
                item.getPickle().getName(); // Initialize pickle in each order item
            });
        });
        
        return orders;
    }
    @Transactional(readOnly = true)
    public List<OrderDetailDTO> getOrderDetailsByOrderId(Long orderId) {
        return orderRepository.findOrderDetailsByOrderId(orderId);
    }

    
    @Transactional
    public Order updateOrderStatus(Long id, Order.OrderStatus status) {
        Order order = getOrderById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }
}
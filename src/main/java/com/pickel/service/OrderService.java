package com.pickel.service;

import com.pickel.dto.OrderRequest;
import com.pickel.entity.Order;
import com.pickel.entity.OrderItem;
import com.pickel.entity.Pickle;
import com.pickel.entity.User;
import com.pickel.exception.ResourceNotFoundException;
import com.pickel.repository.OrderRepository;
import com.pickel.repository.PickleRepository;
import com.pickel.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class OrderService {
    
    @Autowired
    private OrderRepository orderRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PickleRepository pickleRepository;
    
    @Transactional
    public Order createOrder(OrderRequest request, String username) {
        User customer = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        
        Order order = new Order();
        order.setCustomer(customer);
        order.setDeliveryAddress(request.getDeliveryAddress());
        order.setNotes(request.getNotes());
        order.setStatus(Order.OrderStatus.PENDING);
        
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
    
    public List<Order> getCustomerOrders(String username) {
        User customer = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return orderRepository.findByCustomerOrderByCreatedAtDesc(customer);
    }
    
    public Order getOrderById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));
    }
    
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
    
    @Transactional
    public Order updateOrderStatus(Long id, Order.OrderStatus status) {
        Order order = getOrderById(id);
        order.setStatus(status);
        return orderRepository.save(order);
    }
}
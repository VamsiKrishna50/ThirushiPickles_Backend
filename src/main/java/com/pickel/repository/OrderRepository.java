package com.pickel.repository;

import com.pickel.entity.Order;
import com.pickel.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer(User customer);
    List<Order> findByCustomerOrderByCreatedAtDesc(User customer);
    List<Order> findByStatus(Order.OrderStatus status);
}
package com.pickel.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.pickel.dto.OrderDetailDTO;
import com.pickel.entity.Order;
import com.pickel.entity.User;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByCustomer(User customer);
    List<Order> findByCustomerOrderByCreatedAtDesc(User customer);
    List<Order> findByStatus(Order.OrderStatus status);
    
    // UPDATED QUERY - Added mapLink, deliveryCity, deliveryPincode
    @Query("""
        SELECT new com.pickel.dto.OrderDetailDTO(
            o.id,
            u.username,
            u.phoneNumber,
            o.createdAt,
            o.deliveryAddress,
            o.notes,
            o.status,
            o.totalAmount,
            p.name,
            oi.quantity,
            oi.price,
            oi.subtotal,
            o.mapLink,
            o.deliveryCity,
            o.deliveryPincode
        )
        FROM Order o
        JOIN o.customer u
        JOIN o.orderItems oi
        JOIN oi.pickle p
        WHERE o.id = :orderId
    """)
    List<OrderDetailDTO> findOrderDetailsByOrderId(@Param("orderId") Long orderId);
}
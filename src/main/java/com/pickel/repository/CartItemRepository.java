package com.pickel.repository;

import com.pickel.entity.Cart;
import com.pickel.entity.CartItem;
import com.pickel.entity.Pickle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    
    Optional<CartItem> findByCartAndPickle(Cart cart, Pickle pickle);
    
    void deleteByCart(Cart cart);
}
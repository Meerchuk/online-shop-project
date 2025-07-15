package com.example.shop.repository;

import com.example.shop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    Optional<CartItem> findByCartIdAndProductId(Integer cartId, Integer productId);
    void deleteByProductId(Integer productId);

}

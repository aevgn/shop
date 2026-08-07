package org.example.shop.Repository;

import org.example.shop.Entity.Cart;
import org.example.shop.Entity.CartItem;
import org.example.shop.Entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}

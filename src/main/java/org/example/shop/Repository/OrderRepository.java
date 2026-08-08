package org.example.shop.Repository;

import org.example.shop.Entity.Order;
import org.example.shop.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByUser(User user);
    Optional<Order> findByIdAndUserId(Long id, Long userId);
}

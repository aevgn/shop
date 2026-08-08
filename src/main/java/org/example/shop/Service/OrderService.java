package org.example.shop.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.shop.DTO.order.OrderItemResponse;
import org.example.shop.DTO.order.OrderResponse;
import org.example.shop.Entity.*;
import org.example.shop.Enum.OrderStatus;
import org.example.shop.Repository.CartRepository;
import org.example.shop.Repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class OrderService {
    private final OrderRepository orderRepository;
    private final UserService userService;
    private final CartRepository cartRepository;

    @Transactional
    public OrderResponse createOrder(){
        User user = userService.getCurrentUser();
        Cart cart = cartRepository.findByUserId(user.getId()).orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getItems().isEmpty()){
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();

        order.setUser(user);
        order.setStatus(OrderStatus.NEW);
        order.setCreatedAt(LocalDateTime.now());

        BigDecimal totalPrice = BigDecimal.ZERO;

        for (CartItem cartItem : cart.getItems()){
            OrderItem orderItem = new OrderItem();

            orderItem.setOrder(order);
            orderItem.setProduct(cartItem.getProduct());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getProduct().getPrice());
            order.getItems().add(orderItem);

            BigDecimal itemTotalPrice =
                    cartItem.getProduct()
                            .getPrice()
                            .multiply(
                                    BigDecimal.valueOf(cartItem.getQuantity())
                            );

            totalPrice = totalPrice.add(itemTotalPrice);
        }

        order.setTotalPrice(totalPrice);
        Order savedOrder = orderRepository.save(order);
        cart.getItems().clear();
        cartRepository.save(cart);
        return toResponse(savedOrder);
    }

    private OrderResponse toResponse(Order order){
        OrderResponse response = new OrderResponse();

        response.setId(order.getId());
        response.setStatus(order.getStatus());
        response.setCreatedAt(order.getCreatedAt());
        response.setTotalPrice(order.getTotalPrice());
        response.setUserId(order.getUser().getId());
        List<OrderItemResponse> items = order.getItems().stream()
                .map(this::toItemResponse)
                .toList();
        response.setItems(items);
        return response;
    }

    private OrderItemResponse toItemResponse(OrderItem item){
        OrderItemResponse response = new OrderItemResponse();

        response.setId(item.getId());
        response.setQuantity(item.getQuantity());
        response.setPrice(item.getPrice());
        response.setProductId(item.getProduct().getId());
        response.setProductName(item.getProduct().getName());
        response.setItemTotalPrice(
                item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()))
        );

        return response;
    }

    public List<OrderResponse> getOrders() {
        User user = userService.getCurrentUser();
        List<Order> orders = orderRepository.findByUser(user);
        return orders.stream().map(this::toResponse).toList();
    }

    public OrderResponse getOrder(Long id){
        Long userId = userService.getCurrentUserId();
        Order order = orderRepository.findByIdAndUserId(id, userId).orElseThrow(() -> new RuntimeException("Order not found"));
        return toResponse(order);
    }
}

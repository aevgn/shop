package org.example.shop.Controller;

import lombok.AllArgsConstructor;
import org.example.shop.DTO.order.OrderResponse;
import org.example.shop.Service.OrderService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public OrderResponse createOrder(){
        return orderService.createOrder();
    }

    @GetMapping
    public List<OrderResponse> getOrders(){
        return orderService.getOrders();
    }

    @GetMapping("/{id}")
    public OrderResponse getOrder(@PathVariable Long id){
        return orderService.getOrder(id);
    }
}

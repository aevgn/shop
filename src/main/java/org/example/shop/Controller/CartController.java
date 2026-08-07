package org.example.shop.Controller;

import lombok.AllArgsConstructor;
import org.example.shop.DTO.cart.AddToCartRequest;
import org.example.shop.DTO.cart.CartResponse;
import org.example.shop.DTO.cart.UpdateCartItemRequest;
import org.example.shop.Service.CartService;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/cart")
public class CartController {
    private final CartService cartService;

    @GetMapping
    public CartResponse getCart(){
        return cartService.getCart();
    }

    @PostMapping("/items")
    public void addProduct(@RequestBody AddToCartRequest request){
        cartService.addProduct(request);
    }

    @DeleteMapping("/items/{id}")
    public void deleteItem(@PathVariable Long id){
        cartService.deleteItem(id);
    }

    @DeleteMapping
    public void clearCart(){
        cartService.clearCart();
    }

    @PutMapping("/items/{id}")
    public void updateItem(@PathVariable Long id, @RequestBody UpdateCartItemRequest request){
        cartService.updateItem(id, request);
    }
}

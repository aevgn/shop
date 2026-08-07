package org.example.shop.Service;

import lombok.AllArgsConstructor;
import org.example.shop.DTO.cart.AddToCartRequest;
import org.example.shop.DTO.cart.CartItemResponse;
import org.example.shop.DTO.cart.CartResponse;
import org.example.shop.DTO.cart.UpdateCartItemRequest;
import org.example.shop.Entity.Cart;
import org.example.shop.Entity.CartItem;
import org.example.shop.Entity.Product;
import org.example.shop.Repository.CartItemRepository;
import org.example.shop.Repository.CartRepository;
import org.example.shop.Repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserService userService;
    private final ProductRepository productRepository;

    public CartResponse getCart(){
        Cart cart = cartRepository.findByUserId(userService.getCurrentUserId())
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        CartResponse response = new CartResponse();

        response.setId(cart.getId());
        response.setUserId(cart.getUser().getId());

        List<CartItemResponse> items = cart.getItems()
                .stream()
                .map(this::toResponse)
                .toList();

        response.setItems(items);

        BigDecimal totalPrice = items.stream()
                .map(CartItemResponse::getItemTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        response.setTotalPrice(totalPrice);

        return response;
    }

    private CartItemResponse toResponse(CartItem item) {
        CartItemResponse response = new CartItemResponse();

        response.setId(item.getId());
        response.setProductId(item.getProduct().getId());
        response.setProductName(item.getProduct().getName());
        response.setPrice(item.getProduct().getPrice());
        response.setQuantity(item.getQuantity());

        response.setItemTotalPrice(
                item.getProduct()
                        .getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity()))
        );

        return response;
    }

    public void addProduct(AddToCartRequest request){
        if (request.getQuantity() <= 0){
            throw new RuntimeException("Quantity can't be <= 0");
        }
        Cart cart = cartRepository.findByUserId(userService.getCurrentUserId()).orElseThrow(() -> new RuntimeException("Cart not found"));
        Product product = productRepository.findById(request.getProductId()).orElseThrow(() -> new RuntimeException("Product not found"));
        Optional<CartItem> optionalItem = cartItemRepository.findByCartAndProduct(cart, product);
        if (optionalItem.isPresent()){
            CartItem item = optionalItem.get();
            item.setQuantity(item.getQuantity()+ request.getQuantity());
            cartItemRepository.save(item);
        } else{
            CartItem item = new CartItem();
            item.setCart(cart);
            item.setProduct(product);
            item.setQuantity(request.getQuantity());
            cartItemRepository.save(item);
        }
    }

    public void deleteItem(Long id){
        CartItem cartItem = cartItemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));
        Long currentUserId = userService.getCurrentUserId();

        if (!cartItem.getCart().getUser().getId().equals(currentUserId)){
            throw new RuntimeException("Access denied");
        }

        cartItemRepository.deleteById(id);
    }

    public void updateItem(Long id, UpdateCartItemRequest request) {
        CartItem item = cartItemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));
        Long currentUserId = userService.getCurrentUserId();

        if (!item.getCart().getUser().getId().equals(currentUserId)){
            throw new RuntimeException("Access denied");
        }

        item.setQuantity(request.getQuantity());

        cartItemRepository.save(item);
    }

    public void clearCart() {
        Cart cart = cartRepository.findByUserId(userService.getCurrentUserId()).orElseThrow(() -> new RuntimeException("Cart not found"));
        cartItemRepository.deleteAll(cart.getItems());
    }
}

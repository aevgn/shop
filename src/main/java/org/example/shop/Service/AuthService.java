package org.example.shop.Service;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.example.shop.DTO.auth.LoginRequest;
import org.example.shop.DTO.auth.RegisterRequest;
import org.example.shop.Entity.Cart;
import org.example.shop.Entity.User;
import org.example.shop.Repository.CartRepository;
import org.example.shop.Repository.UserRepository;
import org.example.shop.Security.JwtService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;
    private final CartRepository cartRepository;

    @Transactional
    public void register(RegisterRequest request){
        User user = new User();
        user.setEmail(request.getEmail());
        user.setUsername(request.getUsername());
        user.setPassword(encoder.encode(request.getPassword()));
        User savedUser = userRepository.save(user);
        Cart cart = new Cart();
        cart.setUser(savedUser);
        cartRepository.save(cart);
    }

    public String login(LoginRequest request){
        User user = userRepository.findByEmail(request.getEmail()).orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(request.getPassword(), user.getPassword())){
            throw new RuntimeException("Wrong password");
        }

        return jwtService.generateToken(user);
    }
}

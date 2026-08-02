package org.example.shop.Controller;

import org.example.shop.DTO.auth.AuthResponse;
import org.example.shop.DTO.auth.LoginRequest;
import org.example.shop.DTO.auth.RegisterRequest;
import org.example.shop.Service.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public void register(@RequestBody RegisterRequest request){
        authService.register(request);
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody LoginRequest request){
        String token = authService.login(request);

        return new AuthResponse(token);
    }
}

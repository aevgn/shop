package org.example.shop.Service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.example.shop.Entity.User;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class JwtService {
    private final String secret = "d18fa4e79b29c5b3648a3e790a12cf6b52fd8c39e14a27b83d8e5f1b623a9dca";

    public String generateToken(User user){
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("id", user.getId())
                .setIssuedAt(new Date())
                .setExpiration(
                        new Date(System.currentTimeMillis() + 8640000)
                )
                .signWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .compact();
    }
}

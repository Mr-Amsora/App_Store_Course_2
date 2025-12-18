package com.codewithmosh.store.services;

import com.codewithmosh.store.config.JwtConfig;
import com.codewithmosh.store.entities.Role;
import com.codewithmosh.store.entities.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
@AllArgsConstructor
public class JwtService {
    private JwtConfig jwtConfig;

    public Jwt generateAccessToken(User user) {
        return generateToken(user, jwtConfig.getAccessTokenExpiration());
    }

    public Jwt generateRefreshToken(User user) {
        return generateToken(user, jwtConfig.getRefreshTokenExpiration());
    }

    private Jwt generateToken(User user, long EXPIRATION_TIME) {
        var claims = Jwts.claims().subject(user.getId().toString())
                .add("name", user.getName())
                .add("email", user.getEmail())
                .add("role", user.getRole())
                .setIssuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME*1000))
                .build();
        return new Jwt(jwtConfig.getSecretKey(), claims);
    }

    private Claims getClaims(String token) {
        return Jwts.parser().verifyWith(jwtConfig.getSecretKey()).build().parseClaimsJws(token).getPayload();
    }

    public Jwt parseToken(String token) {
        try {
            var claims = getClaims(token);
            return new Jwt(jwtConfig.getSecretKey(), claims);
        } catch (JwtException e) {
            return null;
        }
    }
}

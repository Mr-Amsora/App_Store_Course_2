package com.codewithmosh.store.services;

import com.codewithmosh.store.entities.Role;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

import javax.crypto.SecretKey;
import java.util.Date;

public class Jwt {
    private final SecretKey secretKey;
    private final Claims claims;
    public Jwt(SecretKey secretKey, Claims claims) {
        this.secretKey = secretKey;
        this.claims = claims;
    }

    public Long getUserId() {
        return Long.valueOf(claims.getSubject());
    }

    public Role getUserRole() {
        return Role.valueOf(claims.get("role", String.class));
    }

    public boolean isExpired(String token) {
        return claims.getExpiration().before(new Date());
    }

    @Override
    public String toString() {
        return Jwts.builder().setClaims(claims).signWith(secretKey).compact();
    }
}

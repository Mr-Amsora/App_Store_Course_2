package com.codewithmosh.store.services;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class JwtService {
    @Value("${spring.jwt.expiration}")
    private long EXPIRATION_TIME ;
    @Value("${spring.jwt.secret}")
    private String SECRET ;
    public String generateToken(String email) {
        return Jwts.builder().
                subject(email).
                issuedAt(new Date()).
                expiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME)).
                signWith(Keys.hmacShaKeyFor(SECRET.getBytes())).compact();
    }
}

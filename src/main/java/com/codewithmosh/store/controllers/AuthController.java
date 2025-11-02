package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.JwtResponseDto;
import com.codewithmosh.store.dtos.UserLoginDto;
import com.codewithmosh.store.services.JwtService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(@RequestBody UserLoginDto userDto) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword()));
        var token = jwtService.generateToken(userDto.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(new JwtResponseDto(token));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<?> handleAuthenticationException() {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }
}

package com.codewithmosh.store.controllers;


import com.codewithmosh.store.dtos.CartIdDto;
import com.codewithmosh.store.exceptions.CartEmptyException;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.PaymentException;
import com.codewithmosh.store.repositories.OrderRepository;
import com.codewithmosh.store.services.CheckoutService;
import com.codewithmosh.store.services.WebhookRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/checkout")
public class CheckoutController {
    private final CheckoutService checkoutService;
    private final OrderRepository orderRepository;


    @PostMapping
    public ResponseEntity<?> checkout(@RequestBody CartIdDto idDto) {
        return ResponseEntity.status(HttpStatus.OK).body(checkoutService.checkout(idDto.getCartId()));
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handleWebhook(@RequestHeader Map<String,String> headers , @RequestBody String payload){
        checkoutService.handelWebhookEvent(new WebhookRequest(headers, payload));
        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String,String>> handleException(CartNotFoundException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(CartEmptyException.class)
    public ResponseEntity<Map<String,String>> handleException(CartEmptyException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", exception.getMessage()));
    }

    @ExceptionHandler(PaymentException.class)
    public ResponseEntity<Map<String,String>> handleException(PaymentException exception) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of("error", exception.getMessage()));
    }
}

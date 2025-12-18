package com.codewithmosh.store.controllers;


import com.codewithmosh.store.dtos.AllOrdersDto;
import com.codewithmosh.store.exceptions.OrderNotFoundException;
import com.codewithmosh.store.exceptions.UnAuthorizedAccessException;
import com.codewithmosh.store.services.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @GetMapping
    public ResponseEntity<List<AllOrdersDto>> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<AllOrdersDto> getOrderById(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<String> handleException(OrderNotFoundException exception) {
        return ResponseEntity.status(404).body(exception.getMessage());
    }

    @ExceptionHandler(UnAuthorizedAccessException.class)
    public ResponseEntity<String> handleException(UnAuthorizedAccessException exception) {
        return ResponseEntity.status(403).body(exception.getMessage());
    }
}

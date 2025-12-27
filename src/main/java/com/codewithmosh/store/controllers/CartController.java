package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.AddItemToCartRequest;
import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.dtos.CartItemDto;
import com.codewithmosh.store.dtos.QuantityRequestDto;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.OutOfStockException;
import com.codewithmosh.store.exceptions.ProductNotFoundException;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.services.AuthService;
import com.codewithmosh.store.services.CartService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/carts")
@AllArgsConstructor
@Tag(name = "Carts")
public class CartController {
    private final CartService cartService;
    private final CartRepository cartRepository;
    private final AuthService authService;

    @PostMapping
    public ResponseEntity<CartDto> createCart(UriComponentsBuilder uriComponentsBuilder) {
        var newCartDto = cartService.creatCart();
        var uri = uriComponentsBuilder.path("/carts/{id}").buildAndExpand(newCartDto.getId()).toUri();
        return ResponseEntity.created(uri).body(newCartDto);
    }

    @PostMapping("/{id}/items")
    @Operation(summary = "Adds a product to the cart")
    public ResponseEntity<CartItemDto> addToCart(@Parameter(description = "The ID of the cart") @PathVariable UUID id, @RequestBody AddItemToCartRequest addItemToCartRequest, UriComponentsBuilder uriComponentsBuilder) {
        var response = cartService.addToCart(id, addItemToCartRequest.getProductId());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CartDto> getCart(@PathVariable UUID id) {
        var cartDto = cartService.getCart(id);
        return ResponseEntity.ok(cartDto);
    }

    @PutMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> updateCartItem(@PathVariable UUID cartId, @PathVariable Long productId, @RequestBody @Valid QuantityRequestDto quantity) {
        var cartItemDto = cartService.updateCartItem(cartId, productId, quantity);
        return ResponseEntity.ok(cartItemDto);
    }

    @DeleteMapping("/{cartId}/items/{productId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable UUID cartId ,@PathVariable Long productId) {
        cartService.deleteCartItem(cartId, productId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{cartId}/items")
    public ResponseEntity<?> clearCart(@PathVariable UUID cartId) {
        cartService.clearCart(cartId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/my-carts")
    public ResponseEntity<?> getCartsByUser() {
        List<Cart> carts = cartRepository.getCartsByUser(authService.findCurrentUser()).orElse(null);
        if (carts == null || carts.isEmpty()) {
            throw new CartNotFoundException("No carts found for the user");
        }
        return ResponseEntity.ok(carts);
    }




    @ExceptionHandler(CartNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleCartNotFound() {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Cart not found"));
    }

    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleProductNotFound() {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Product not found in the cart"));
    }

    @ExceptionHandler(OutOfStockException.class)
    public ResponseEntity<Map<String, String>> handleOutOfStock(OutOfStockException exception) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", exception.getMessage()));
    }

}

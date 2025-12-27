package com.codewithmosh.store.services;

import com.codewithmosh.store.dtos.CartDto;
import com.codewithmosh.store.dtos.CartItemDto;
import com.codewithmosh.store.dtos.QuantityRequestDto;
import com.codewithmosh.store.entities.Cart;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.ProductNotFoundException;
import com.codewithmosh.store.mappers.CartItemMapper;
import com.codewithmosh.store.mappers.CartMapper;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@AllArgsConstructor
public class CartService {

    private final UserService userService;
    private final AuthService authService;
    private CartRepository cartRepository;
    private CartMapper cartMapper;
    private ProductRepository productRepository;
    private CartItemMapper cartItemMapper;

    public CartDto creatCart() {
        var cart = new Cart();
        cart.setUser(authService.findCurrentUser());
        cartRepository.save(cart);
        return cartMapper.toCartDto(cart);
    }

    public CartItemDto addToCart(UUID cartId, long productId) {
        var cart = cartRepository.findById(cartId).orElseThrow(
                () -> new CartNotFoundException("Cart not found")
        );

        var product = productRepository.findById(productId).orElseThrow(
                () -> new ProductNotFoundException("Product not found")
        );

        var cartItem = cart.addItem(product);

        cartRepository.save(cart);

        return cartItemMapper.toDto(cartItem);
    }


    public CartDto getCart(UUID cartId) {
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException("Cart not found");
        }
        return cartMapper.toCartDto(cart);
    }

    public CartItemDto updateCartItem(UUID cartId, Long productId, QuantityRequestDto quantity) {
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            throw  new CartNotFoundException("Cart not found");
        }
        var cartItem = cart.getItemByProductId(productId);
        if (cartItem == null) {
            throw  new ProductNotFoundException("Product not found");
        }
        cartItem.setQuantity(quantity.getQuantity());
        cartRepository.save(cart);
        return cartItemMapper.toDto(cartItem);

    }

    public void deleteCartItem(UUID cartId, Long productId) {
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException("Cart not found");
        }
        cart.removeItem(productId);
        cartRepository.save(cart);
    }

    public void clearCart(UUID cartId) {
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException("Cart not found");
        }
        cart.clear();
        cartRepository.save(cart);
    }
}

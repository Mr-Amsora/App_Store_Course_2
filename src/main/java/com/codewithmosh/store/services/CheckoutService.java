package com.codewithmosh.store.services;


import com.codewithmosh.store.dtos.CheckoutResponseDto;
import com.codewithmosh.store.entities.Order;
import com.codewithmosh.store.entities.PaymentStatus;
import com.codewithmosh.store.exceptions.CartEmptyException;
import com.codewithmosh.store.exceptions.CartNotFoundException;
import com.codewithmosh.store.exceptions.PaymentException;
import com.codewithmosh.store.exceptions.UnAuthorizedAccessException;
import com.codewithmosh.store.repositories.CartRepository;
import com.codewithmosh.store.repositories.OrderRepository;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CheckoutService {
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final AuthService authService;
    private final CartService cartService;
    private final PaymentGateway paymentGateway;
    private final ProductRepository productRepository;


    @Transactional
    public CheckoutResponseDto checkout(UUID cartId){
        var cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            throw new CartNotFoundException("Invalid cart ID");
        }
        if (cart.getItems().isEmpty()) {
            throw new CartEmptyException("Cart is empty");
        }
        var user = authService.findCurrentUser();
        if (user == null) {
            throw new UnAuthorizedAccessException("User must be authenticated to checkout");
        }
        var order = new Order();
        order = order.saveOrder(cart, user);

        orderRepository.save(order);

        try {
            var session = paymentGateway.createCheckoutSession(order);
            cartService.clearCart(cart.getId());
            var response = new CheckoutResponseDto();
            response.setOrderId(order.getId());
            response.setCheckoutUrl(session.getCheckoutUrl());
            return response;

        }catch (PaymentException e){
            orderRepository.delete(order);
            throw e;
        }
    }

    public void handelWebhookEvent(WebhookRequest request){
        paymentGateway.parseWebhookRequest(request).ifPresent( paymentResult -> {
            var order = orderRepository.findById(paymentResult.getOrderId()).orElse(null);
            order.setStatus(paymentResult.getPaymentStatus());
            orderRepository.save(order);
            if (paymentResult.getPaymentStatus() == PaymentStatus.PAID) {
                order.getItems().forEach(item -> {
                    var product = item.getProduct();
                    int newQuantity = product.getQuantity() - item.getQuantity();

                    if (newQuantity <= 0) {
                        productRepository.delete(product);
                    } else {
                        product.setQuantity(newQuantity);
                        productRepository.save(product);
                    }
                });
            }
        });
    }

}

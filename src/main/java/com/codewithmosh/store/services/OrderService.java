package com.codewithmosh.store.services;

import com.codewithmosh.store.dtos.AllOrderItemsDto;
import com.codewithmosh.store.dtos.AllOrdersDto;
import com.codewithmosh.store.dtos.ItemsProductsDto;
import com.codewithmosh.store.entities.PaymentStatus;
import com.codewithmosh.store.exceptions.OrderNotFoundException;
import com.codewithmosh.store.exceptions.UnAuthorizedAccessException;
import com.codewithmosh.store.repositories.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderService {

    private AuthService authService;
    private OrderRepository orderRepository;

    public List<AllOrdersDto> getAllOrders() {
        var user = authService.findCurrentUser();
        var orders =orderRepository.getOrderByCustomerId(user);
        List<AllOrdersDto> response = new ArrayList<>();
        orders.forEach(order -> {
            var orderDto = new AllOrdersDto();
            orderDto.setId(order.getId());
            orderDto.setTotalPrice(order.getTotalPrice());
            orderDto.setCreatedAt(order.getCreatedAt());
            orderDto.setStatus(PaymentStatus.valueOf(order.getStatus().name()));
            var products = order.getItems().stream().map(item -> new AllOrderItemsDto(new ItemsProductsDto(
                    item.getProduct().getId(),
                    item.getProduct().getName(),
                    item.getUnitPrice()
            ) , item.getQuantity() , item.getTotalPrice())).toList();
            orderDto.setItems(products);
            response.add(orderDto);
        });
        return response;
    }

    public AllOrdersDto getOrderById(Long orderId) {
        var order = orderRepository.getOrderWithItems(orderId).orElseThrow(() -> new OrderNotFoundException("Order with id " + orderId + " not found"));
        if (order.getCustomer().getId() != authService.findCurrentUser().getId()) {
            throw new UnAuthorizedAccessException("You are not authorized to access this order");
        }
        var orderDto = new AllOrdersDto();
        orderDto.setId(order.getId());
        orderDto.setTotalPrice(order.getTotalPrice());
        orderDto.setCreatedAt(order.getCreatedAt());
        orderDto.setStatus(PaymentStatus.valueOf(order.getStatus().name()));
        var products = order.getItems().stream().map(item -> new AllOrderItemsDto(new ItemsProductsDto(
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getUnitPrice()
        ) , item.getQuantity() , item.getTotalPrice())).toList();
        orderDto.setItems(products);
        return orderDto;

    }
}

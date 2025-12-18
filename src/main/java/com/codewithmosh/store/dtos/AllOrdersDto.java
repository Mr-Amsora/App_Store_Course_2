package com.codewithmosh.store.dtos;

import com.codewithmosh.store.entities.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class AllOrdersDto {
    private Long id;
    private PaymentStatus status;
    private LocalDateTime createdAt;
    private List<AllOrderItemsDto> items;
    private BigDecimal totalPrice;
}

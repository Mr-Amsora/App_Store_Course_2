package com.codewithmosh.store.services;

import com.codewithmosh.store.entities.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@AllArgsConstructor
@Data
public class PaymentResult {
    private Long orderId;
    private PaymentStatus paymentStatus;
}

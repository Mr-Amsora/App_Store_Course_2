package com.codewithmosh.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AllOrderItemsDto {
    private ItemsProductsDto product;
    private Integer quantity;
    private BigDecimal totalPrice;
}

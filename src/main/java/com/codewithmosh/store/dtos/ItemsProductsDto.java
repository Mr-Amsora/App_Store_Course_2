package com.codewithmosh.store.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ItemsProductsDto {
    private Long id;
    private String name;
    private BigDecimal price;
}

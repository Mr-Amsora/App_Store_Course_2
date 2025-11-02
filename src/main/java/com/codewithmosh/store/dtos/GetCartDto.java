package com.codewithmosh.store.dtos;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class GetCartDto {
    private UUID id;
    private List<CartItemDto> items= new ArrayList<>();
}

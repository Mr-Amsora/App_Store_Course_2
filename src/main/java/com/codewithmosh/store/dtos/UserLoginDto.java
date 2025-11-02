package com.codewithmosh.store.dtos;

import lombok.Data;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Data
public class UserLoginDto {
    private String email;
    private String password;
}

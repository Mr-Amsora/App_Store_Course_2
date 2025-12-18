package com.codewithmosh.store.services;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.Map;


@Getter
@AllArgsConstructor
public class WebhookRequest {
    private Map<String,String> headers;
    private String payload;
}

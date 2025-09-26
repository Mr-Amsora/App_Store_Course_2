package com.codewithmosh.store.controllers;

import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.mappers.ProductMapper;
import com.codewithmosh.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @GetMapping
    public List<ProductDto> fetchAllProducts(@RequestParam(required = false , name = "categoryId")  Long categoryId) {
        if (categoryId == null) {
            return productRepository.findAll().stream().map(product -> productMapper.toDto(product)).toList();
        }
        return productRepository.findAll(Sort.by("category_id")).stream().map(product -> productMapper.toDto(product)).toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> fetchProduct(@PathVariable Long id) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(productMapper.toDto(product));
        }

    }
}

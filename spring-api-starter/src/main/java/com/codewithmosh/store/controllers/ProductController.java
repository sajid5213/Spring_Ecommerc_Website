package com.codewithmosh.store.controllers;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.mappers.ProductMapper;
import com.codewithmosh.store.repositories.ProductRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor 
@RestController
@RequestMapping ("/products")
public class ProductController { 
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    @GetMapping 
    public List<ProductDto> getAllProducts(
        @RequestParam(required = false, name = "categoryId") Byte categoryId  
    ) {
        List<Product> products;
        if(categoryId != null) {
            products = productRepository.findByCategoryId(categoryId);
        } else {
            products = productRepository.findAllwithCategory();
        }
        return products .stream().map(productMapper::toDto).toList();
    }

    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable Long id) {
        var product = productRepository.findById(id).orElse(null);
        if(product == null) {
            return null;
        } 
        return productMapper.toDto(product); 
    }  
}
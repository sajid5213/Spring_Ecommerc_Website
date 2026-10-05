package com.codewithmosh.store.controllers;

import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.codewithmosh.store.dtos.ProductDto;
import com.codewithmosh.store.entities.Product;
import com.codewithmosh.store.mappers.ProductMapper;
import com.codewithmosh.store.repositories.CategoryRepository;
import com.codewithmosh.store.repositories.ProductRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor 
@RestController
@RequestMapping ("/products")
public class ProductController { 
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    public final CategoryRepository categoryRepository;

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

    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
        @RequestBody ProductDto productDto,
        UriComponentsBuilder uriBuilder
    ) {
       var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
       if(category == null) {
            return ResponseEntity.badRequest().build();
       }

       var product = productMapper.toEntity(productDto);
       product.setCategory(category);

       productRepository.save(product);
       productDto.setId(product.getId());

       var uri = uriBuilder.path("/products/{id}").buildAndExpand(product.getId()).toUri();
       return ResponseEntity.created(uri).body(productDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
        @PathVariable Long id,
        @RequestBody ProductDto productDto
    ) {
        var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if(category == null) {
            return ResponseEntity.badRequest().build();
        }

        var product = productRepository.findById(id).orElse(null);
        if(product == null) {
            return ResponseEntity.notFound().build();
        }

        productMapper.update(productDto, product);
        product.setCategory(category);
        productRepository.save(product);
        productDto.setId(product.getId());

        return ResponseEntity.ok(productDto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        var product = productRepository.findById(id).orElse(null);
        if(product == null) {
            return ResponseEntity.notFound().build();
        }
        productRepository.delete(product);
        return ResponseEntity.noContent().build();
    }  
} 
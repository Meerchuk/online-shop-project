package com.example.shop.controller;

import com.example.shop.dto.productDto.ProductResponseDto;
import com.example.shop.entity.Product;
import com.example.shop.mapper.ProductMapper;
import com.example.shop.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/products")
public class ProductViewController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    @Autowired
    public ProductViewController(ProductService productService, ProductMapper productMapper) {
        this.productService = productService;
        this.productMapper = productMapper;
    }

    @GetMapping
    public String showProducts(Model model) {
        List<Product> products = productService.getAllProducts();
        List<ProductResponseDto> productDtos = products.stream()
                .map(productMapper::toDto)
                .collect(Collectors.toList());
        model.addAttribute("products", productDtos);
        return "product";
    }
}


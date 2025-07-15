package com.example.shop.controller;

import com.example.shop.dto.productDto.ProductRequestDto;
import com.example.shop.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin/products")
public class AdminProductController {

    private final ProductService productService;

    @Autowired
    public AdminProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/add")
    public String showAddProductForm(Model model) {
        model.addAttribute("product", new ProductRequestDto());
        return "add-product";
    }


    @PostMapping("/add")
    public String addProduct(@ModelAttribute("product") @Valid ProductRequestDto dto,
                             BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "add-product";
        }
        productService.createProduct(dto);
        return "redirect:/admin/products";
    }


    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Integer id) {
        productService.deleteProduct(id);
        return "redirect:/admin/products";
    }



    @GetMapping
    public String listProducts(Model model) {
        model.addAttribute("products", productService.getAllProducts());
        return "product";
    }


}



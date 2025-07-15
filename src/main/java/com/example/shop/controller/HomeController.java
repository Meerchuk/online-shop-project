package com.example.shop.controller;

import com.example.shop.dto.productDto.ProductResponseDto;
import com.example.shop.entity.User;
import com.example.shop.mapper.ProductMapper;
import com.example.shop.service.ProductService;
import com.example.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;
import java.util.List;

@Controller
public class HomeController {

    private final ProductService productService;
    private final UserService userService;
    private final ProductMapper productMapper;

    @Autowired
    public HomeController(ProductService productService,
                          UserService userService,
                          ProductMapper productMapper) {
        this.productService = productService;
        this.userService = userService;
        this.productMapper = productMapper;
    }


    @GetMapping("/")
    public String showHomePage(Model model, Principal principal) {
        List<ProductResponseDto> products = productService.getAllProducts()
                .stream()
                .map(productMapper::toDto)
                .toList();

        model.addAttribute("products", products);

        if (principal != null) {
            String username = principal.getName();
            User user = userService.findByUsername(username);
            model.addAttribute("user", user);
            model.addAttribute("cartId", user.getCart().getId());
        }

        return "home";
    }
}

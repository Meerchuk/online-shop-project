package com.example.shop.controller;

import com.example.shop.service.CartItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart-items")
public class CartItemViewController {

    private final CartItemService cartItemService;

    @Autowired
    public CartItemViewController(CartItemService cartItemService) {
        this.cartItemService = cartItemService;
    }


    @PostMapping("/add")
    public String addToCart(@RequestParam Integer productId,
                            @RequestParam Integer quantity,
                            @RequestParam Integer cartId) {

        cartItemService.addOrUpdateItem(cartId, productId, quantity);
        return "redirect:/";
    }
}


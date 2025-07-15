package com.example.shop.controller;

import com.example.shop.service.CartItemService;
import com.example.shop.service.UserService;
import com.example.shop.entity.User;
import org.springframework.ui.Model;
import com.example.shop.dto.cartDto.CartDto;
import com.example.shop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("/cart")
public class CartViewController {

    private final CartService cartService;
    private final UserService userService;
    private final CartItemService cartItemService;



    @Autowired
    public CartViewController(CartService cartService, UserService userService, CartItemService cartItemService) {
        this.cartService = cartService;
        this.userService = userService;
        this.cartItemService = cartItemService;
    }


    @GetMapping("/{userId}")
    public String showCart(@PathVariable Integer userId, Model model) {
        CartDto cart = cartService.getCartDtoForUser(userId);
        model.addAttribute("cart", cart);
        return "cart";
    }


    @PostMapping("/item/delete")
    public String deleteCartItem(@RequestParam Integer cartId,
                                 @RequestParam Integer productId) {
        cartItemService.removeItem(cartId, productId);
        return "redirect:/cart/" + cartId;
    }


    @PostMapping("/checkout")
    public String checkout(Principal principal, Model model) {
        if (principal == null) {
            return "redirect:/users/login";
        }

        String username = principal.getName();
        User user = userService.findByUsername(username);

        try {
            cartService.checkout(user.getId());
        } catch (IllegalArgumentException ex) {
            model.addAttribute("error", ex.getMessage());
            CartDto cart = cartService.getCartDtoForUser(user.getId());
            model.addAttribute("cart", cart);
            return "cart";
        }

        return "redirect:/?success";
    }

}

package com.example.shop.controller;

import com.example.shop.entity.User;
import com.example.shop.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.security.Principal;

@Controller
@RequestMapping("/user")
public class UserProfileController {

    private final UserService userService;

    public UserProfileController(UserService userService) {
        this.userService = userService;
    }


    @GetMapping("/profile")
    public String showUserProfile(Model model, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        String username = principal.getName();
        User user = userService.findByUsername(username);

        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("user", user);
        return "user-profile";
    }


    @PostMapping("/update-balance")
    public String updateBalance(@RequestParam("amount") int amount, Principal principal) {
        if (principal == null) {
            return "redirect:/login";
        }

        String username = principal.getName();
        User user = userService.findByUsername(username);

        if (user == null) {
            return "redirect:/login";
        }

        int newBalance = (user.getCashBalance() == null ? 0 : user.getCashBalance()) + amount;
        userService.updateCashBalance(user.getId(), newBalance);

        return "redirect:/user/profile";
    }

}

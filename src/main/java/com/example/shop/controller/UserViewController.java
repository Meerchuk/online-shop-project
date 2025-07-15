package com.example.shop.controller;

import jakarta.validation.Valid;
import com.example.shop.dto.userDto.UserRegisterRequestDto;
import com.example.shop.dto.userDto.UserLoginRequestDto;
import com.example.shop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserViewController {

    private final UserService userService;

    @Autowired
    public UserViewController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model){
        model.addAttribute("user", new UserRegisterRequestDto());
        return "register";
    }


    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") @Valid UserRegisterRequestDto requestDto,
                               BindingResult bindingResult,
                               Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("errors", bindingResult.getAllErrors());
            return "register";
        }

        userService.registerUser(requestDto);
        return "redirect:/users/login";
    }


    @GetMapping("/login")
    public String showLoginForm(Model model, String error, String logout) {
        model.addAttribute("user", new UserLoginRequestDto());

        if (error != null) {
            model.addAttribute("error", "Invalid login or password");
        }
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully");
        }
        return "login";
    }
}

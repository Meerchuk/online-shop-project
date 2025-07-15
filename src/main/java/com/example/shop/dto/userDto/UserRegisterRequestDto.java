package com.example.shop.dto.userDto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserRegisterRequestDto {

    @NotBlank(message = "Login is required")
    private String login;

    @NotBlank(message = "User name is required")
    private String name;

    @NotBlank(message = "Password is required")
    @Size(min = 4, message = "Password must be at least 4 characters")
    private String password;

    public UserRegisterRequestDto() {
    }

    public UserRegisterRequestDto(String login, String name, String password) {
        this.login = login;
        this.name = name;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

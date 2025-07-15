package com.example.shop.dto.userDto;

public class UserResponseDto {
    private Integer id;
    private String username;
    private Integer cashBalance;

    public UserResponseDto() {
    }

    public UserResponseDto(Integer id, String username, Integer cashBalance) {
        this.id = id;
        this.username = username;
        this.cashBalance = cashBalance;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Integer getCashBalance() {
        return cashBalance;
    }

    public void setCashBalance(Integer cashBalance) {
        this.cashBalance = cashBalance;
    }
}

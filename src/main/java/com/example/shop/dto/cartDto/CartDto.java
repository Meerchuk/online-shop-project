package com.example.shop.dto.cartDto;

import com.example.shop.dto.cartItemDto.CartItemDto;

import java.util.List;

public class CartDto {
    private Integer id;
    private List<CartItemDto> items;
    private Integer totalPrice;

    public CartDto() {
    }

    public CartDto(Integer id, List<CartItemDto> items, Integer totalPrice) {
        this.id = id;
        this.items = items;
        this.totalPrice = totalPrice;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public List<CartItemDto> getItems() {
        return items;
    }

    public void setItems(List<CartItemDto> items) {
        this.items = items;
    }

    public Integer getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice = totalPrice;
    }
}

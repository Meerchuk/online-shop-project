package com.example.shop.dto.productDto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.*;

public class ProductRequestDto {

    @NotBlank(message = "Name could not be empty")
    private String name;


    @NotNull(message = "Price could not be empty")
    @Min(value = 1, message = "Minimal price 1")
    private Integer price;

    @NotNull(message = "Quantity required")
    @Min(value = 0, message = "Quantity could not be negative")
    private Integer quantity;

    private String imageUrl;

    public ProductRequestDto() {}

    public ProductRequestDto(String name, Integer price, Integer quantity, String imageUrl) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageUrl = imageUrl;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}


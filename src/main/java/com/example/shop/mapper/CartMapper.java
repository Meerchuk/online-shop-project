package com.example.shop.mapper;

import com.example.shop.dto.cartDto.CartDto;
import com.example.shop.dto.cartItemDto.CartItemDto;
import com.example.shop.entity.Cart;
import com.example.shop.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CartMapper {

    @Mapping(source = "items", target = "items")
    @Mapping(expression = "java(calculateTotalPrice(cart.getItems()))", target = "totalPrice")
    CartDto toDto(Cart cart);

    List<CartItemDto> toCartItemDtoList(List<CartItem> items);

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.price", target = "productPrice")
    @Mapping(source = "product.imageUrl", target = "productImageUrl")
    CartItemDto toCartItemDto(CartItem item);


    default int calculateTotalPrice(List<CartItem> items) {
        if (items == null) return 0;
        return items.stream()
                .mapToInt(item -> item.getProduct().getPrice() * item.getQuantity())
                .sum();
    }
}

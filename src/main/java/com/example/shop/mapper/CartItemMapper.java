package com.example.shop.mapper;

import com.example.shop.dto.cartItemDto.CartItemDto;
import com.example.shop.entity.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    @Mapping(source = "product.id", target = "productId")
    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.price", target = "productPrice")
    @Mapping(source = "product.imageUrl", target = "productImageUrl")
    CartItemDto toDto(CartItem cartItem);

}

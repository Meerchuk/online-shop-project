package com.example.shop.mapper;

import com.example.shop.dto.productDto.ProductRequestDto;
import com.example.shop.dto.productDto.ProductResponseDto;
import com.example.shop.entity.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {

    ProductResponseDto toDto(Product product);
    Product toEntity(ProductResponseDto dto);
    Product toEntity(ProductRequestDto dto);

}

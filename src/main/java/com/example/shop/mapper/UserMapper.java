package com.example.shop.mapper;

import com.example.shop.dto.userDto.UserRegisterRequestDto;
import com.example.shop.dto.userDto.UserResponseDto;
import com.example.shop.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User toEntity(UserRegisterRequestDto request);
    UserResponseDto toDto(User user);
}

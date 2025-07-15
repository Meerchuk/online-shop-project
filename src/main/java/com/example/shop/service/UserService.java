package com.example.shop.service;

import com.example.shop.dto.userDto.UserLoginRequestDto;
import com.example.shop.dto.userDto.UserRegisterRequestDto;
import com.example.shop.dto.userDto.UserResponseDto;
import com.example.shop.entity.Cart;
import com.example.shop.entity.User;
import com.example.shop.mapper.UserMapper;
import com.example.shop.repository.CartRepository;
import com.example.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;


    @Autowired
    public  UserService(UserRepository userRepository, CartRepository cartRepository, UserMapper userMapper, PasswordEncoder passwordEncoder){
        this.cartRepository = cartRepository;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }


    public User findUserById(Integer id){
        User user = userRepository.findById(id).
                orElseThrow(() -> new IllegalArgumentException("User not found"));
        return user;
    }


    @Transactional
    public UserResponseDto registerUser(UserRegisterRequestDto requestDto) {
        if (userRepository.existsByLogin(requestDto.getLogin())) {
            throw new IllegalArgumentException("User with such login already exists");
        }

        String encodedPassword = passwordEncoder.encode(requestDto.getPassword());
        requestDto.setPassword(encodedPassword);

        User user = userMapper.toEntity(requestDto);
        Cart cart = new Cart(user);
        user.setCart(cart);
        User savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
    }




    public UserResponseDto login(UserLoginRequestDto requestDto){
        User user = userRepository.findByLogin(requestDto.getLogin())
                .orElseThrow(() -> new IllegalArgumentException("Invalid login or password"));

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid login or password");
        }

        return userMapper.toDto(user);
    }



    public UserResponseDto getUserById(Integer id){
        User user = findUserById(id);
        return userMapper.toDto(user);
    }


    public User findByUsername(String username) {
        return userRepository.findByName(username)
                .orElseThrow(() -> new IllegalArgumentException("User not found with username: " + username));
    }


    @Transactional
    public void updateCashBalance(Integer id, int newBalance){
        User user = findUserById(id);
        user.setCashBalance(newBalance);
        userRepository.save(user);
    }


    @Transactional
    public void deleteUser(Integer id){
        if(!userRepository.existsById(id))
            throw new IllegalArgumentException("User not found");
        userRepository.deleteById(id);
    }

}

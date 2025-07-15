package com.example.shop.security;

import com.example.shop.entity.User;
import com.example.shop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        System.out.println("Trying to find user: " + login);
        User user = userRepository.findByLogin(login)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with login: " + login));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getLogin())
                .password(user.getPassword())
                .roles("USER")
                .build();
    }
}

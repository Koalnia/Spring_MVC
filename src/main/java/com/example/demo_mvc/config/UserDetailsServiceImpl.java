package com.example.demo_mvc.config;

import com.example.demo_mvc.dto.UserRegistrationDto;
import com.example.demo_mvc.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserDetailsServiceImpl extends UserDetailsService {

    User save(UserRegistrationDto registrationDto);
    List<User> getAll();
}



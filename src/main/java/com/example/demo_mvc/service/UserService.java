package com.example.demo_mvc.service;

import com.example.demo_mvc.dto.UserRegistrationDto;
import com.example.demo_mvc.dto.UserShowDto;
import com.example.demo_mvc.model.Role;
import com.example.demo_mvc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import com.example.demo_mvc.model.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    public boolean isAdmin(Principal principal) {
        UserDetails userDetails = (UserDetails) ((Authentication) principal).getPrincipal();
        return userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ADMIN"));
    }

    public List<User> findUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .filter(user -> user.getRoles().contains(Role.USER.name()))
                .collect(Collectors.toList());
    }

    public UserShowDto convertToDto(User user) {
        UserShowDto dto = new UserShowDto();
        dto.setId(user.getId());
        dto.setName(user.getName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setEmail(user.getEmail());
        return dto;
    }

    public String updateUser(User user, UserRegistrationDto userRegistrationDto, Principal principal) {
        String oldName = user.getEmail();
        user.setName(userRegistrationDto.getName());
        user.setPhoneNumber(userRegistrationDto.getPhoneNumber());
        user.setEmail(userRegistrationDto.getEmail());
        user.setPassword(passwordEncoder.encode(userRegistrationDto.getPassword()));
        try {
            userRepository.save(user);}
        catch(Exception e) {
            return "redirect:/user/update/"+user.getId()+"?email_invalid";
        }
        System.out.println(user.getPassword());
        if(principal.getName().equals(oldName))
            return "redirect:/logout";

        return "redirect:/user/index?success";
    }
}

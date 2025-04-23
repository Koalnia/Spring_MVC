package com.example.demo_mvc.controller;

import com.example.demo_mvc.dto.UserRegistrationDto;
import com.example.demo_mvc.config.UserDetailsServiceImpl;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/registration")
public class RegistrationController {

    private UserDetailsServiceImpl userDetailsServiceImpl;

    public RegistrationController(UserDetailsServiceImpl userDetailsServiceImpl) {
        super();
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @ModelAttribute("user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }

    @GetMapping
    public String showRegistrationForm() {
        return "registration";
    }

    @PostMapping
    public String registerUserAccount(@ModelAttribute("user")
                                     @Valid UserRegistrationDto registrationDto, Errors errores){
        if(errores.hasErrors()){
            return "registration";
        }
        try {
            userDetailsServiceImpl.save(registrationDto);}
        catch(Exception e) {
            return "redirect:/registration?email_invalid";
        }
        return "redirect:/login?success";
    }
}
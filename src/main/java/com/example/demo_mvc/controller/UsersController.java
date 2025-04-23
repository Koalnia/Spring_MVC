package com.example.demo_mvc.controller;

import com.example.demo_mvc.dto.UserRegistrationDto;
import com.example.demo_mvc.dto.UserShowDto;
import com.example.demo_mvc.model.User;
import com.example.demo_mvc.repository.UserRepository;
import com.example.demo_mvc.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/user")
public class UsersController {
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserService userService;

    @GetMapping("/index")
    public String showUserList(Model model, Principal principal) {

        UserShowDto currentUserDto = userService.convertToDto(
                userRepository.findByEmail(principal.getName()));
        model.addAttribute("current_user", currentUserDto);
        if (userService.isAdmin(principal)) {
            List<UserShowDto> userDtoList = userService.findUsers()
                    .stream()
                    .map(userService::convertToDto)
                    .toList();
            model.addAttribute("users", userDtoList.stream()
                    .sorted(Comparator.comparing(UserShowDto::getName))
                    .collect(Collectors.toList()));
        }
        return "index_user";
    }

   @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model , Principal principal) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        if (!principal.getName().equals(user.getEmail()) &&
                !userService.isAdmin(principal)) {
            throw new AccessDeniedException("Nie masz dostępu do edycji tego użytkownika.");
        }
        UserRegistrationDto userRegistrationDto = new UserRegistrationDto(user);
        model.addAttribute("edit_user", userRegistrationDto);
        return "update-user";
    }
    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") long id, @ModelAttribute("edit_user") @Valid UserRegistrationDto userRegistrationDto,
                                Errors errors, Principal principal) {
        if(errors.hasErrors()){
            return "update-user";
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        if (!principal.getName().equals(user.getEmail()) &&
                !userService.isAdmin(principal)) {
            throw new AccessDeniedException("Nie masz dostępu do edycji tego użytkownika.");
        }
        return userService.updateUser(user,userRegistrationDto,principal);
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") long id, Model model, Principal principal) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user Id:" + id));
        if (principal.getName().equals(user.getEmail()) ||
                !userService.isAdmin(principal)) {
            throw new AccessDeniedException("Nie masz dostępu do edycji tego użytkownika.");
        }
        userRepository.delete(user);
        return "redirect:/user/index";
    }

    @ModelAttribute("edit_user")
    public UserRegistrationDto userRegistrationDto() {
        return new UserRegistrationDto();
    }






}

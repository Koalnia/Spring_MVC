package com.example.demo_mvc.dto;

import com.example.demo_mvc.model.User;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public class UserRegistrationDto {

    private Long id;

    @NotNull(message = "Imię nie może być puste")
    @Size(min = 10,message = "Imię musi zawierać co najmniej 10 znaków")
    private String name;

    @NotNull(message = "Numer telefonu nie może być pusty")
    @Size(min = 9,max = 12, message = "Numer telefonu musi zawierać od 9 do 12 znaków")
    private String phoneNumber;

    @NotNull(message = "E-mail nie może być pusty")
    @Size(min = 8,max = 50, message = "E-mail musi zawierać od 8 do 50 znaków")
    @Email
    private String email;

    @NotNull(message = "Hasło nie może być puste")
    @Pattern(regexp ="^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[#$@!%&*?])[A-Za-z\\d#$@!%&*?]{8,20}$",
            message = "Hasło musi zawierać: 1 dużą literę, 1 małą, 1 liczbę, 1 znak specjalny oraz mieć od 8 do 16 znaków")
    private String password;

    public UserRegistrationDto() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



    public UserRegistrationDto (User user)
    {
        this.id = user.getId();
        this.name = user.getName();
        this.phoneNumber = user.getPhoneNumber();
        this.email = user.getEmail();
        this.password = "";
    }
}
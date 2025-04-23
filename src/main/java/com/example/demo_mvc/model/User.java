package com.example.demo_mvc.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "users", uniqueConstraints =
@UniqueConstraint(columnNames = "email"))
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Advertisement> advertisements;

    @NotNull(message = "Imię nie może być puste")
    @Column(name = "first_name")
    private String name;

    @NotNull(message = "Numer telefonu nie może być pusty")
    @Column(name = "phoneNumber")
    private String phoneNumber;

    @NotNull(message = "E-mail nie może być pusty")
    @Email
    private String email;

    @NotNull(message = "Hasło nie może być puste")
    private String password;

    private Collection<String> roles;

    public User() {

    }

    public User(Long id, List<Advertisement> advertisements, String name, String phoneNumber, String email, String password, Collection<String> roles) {
        this.id = id;
        this.advertisements = advertisements;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public User(String name, String phoneNumber, String email, String password, Collection<String> roles) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Advertisement> getAdvertisements() {
        return advertisements;
    }

    public void setAdvertisements(List<Advertisement> advertisements) {
        this.advertisements = advertisements;
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

    public Collection<String> getRoles() {
        return roles;
    }

    public void setRoles(Collection<String> roles) {
        this.roles = roles;
    }
}
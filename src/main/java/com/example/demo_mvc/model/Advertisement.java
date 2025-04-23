package com.example.demo_mvc.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "advertisements")
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull(message = "Tytuł ogłoszenia nie może być pusty")
    @Column(name = "title")
    private String title;

    @NotNull(message = "Opis ogłoszenia nie może być pusty")
    @Column(name = "description")
    private String description;

    @NotNull(message = "Cena ogłoszenia nie może być pusty")
    @Column(name = "price")
    private String price;

    @NotNull(message = "Czas trwania ogłoszenia nie może być pusty")
    @Column(name = "duration")
    private String duration;

    @Column(name = "created_at")
    private String createdAt;

    public Advertisement() {
    }


    public Advertisement(Long id, User user, String title, String description, String price, String duration, String createdAt) {
        this.id = id;
        this.user = user;
        this.title = title;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createdAt = createdAt;
    }

    public Advertisement(Long id, String title, String description, String price, String duration) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.duration = duration;
    }

    public Advertisement(User user, String title, String description, String price, String duration, String createdAt) {
        this.user = user;
        this.title = title;
        this.description = description;
        this.price = price;
        this.duration = duration;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}






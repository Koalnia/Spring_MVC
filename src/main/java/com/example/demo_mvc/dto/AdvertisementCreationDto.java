package com.example.demo_mvc.dto;

import com.example.demo_mvc.model.Advertisement;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class AdvertisementCreationDto {


    private Long id;

    @NotNull(message = "Tytuł ogłoszenia nie może być pusty")
    @Size(min = 8,max = 50, message = "Tytuł ogłoszenia musi zawierać od 8 do 50 znaków")
    private String title;

    @NotNull(message = "Opis ogłoszenia nie może być pusty")
    @Size(min = 9,max = 100, message = "Opis ogłoszenia musi zawierać od 9 do 50 znaków")
    private String description;

    @NotNull(message = "Cena ogłoszenia nie może być pusty")
    @Size(min = 1,max = 50, message = "Cena ogłoszenia musi zawierać od 1 do 50 znaków")
    private String price;

    @NotNull(message = "Czas trwania ogłoszenia nie może być pusty")
    @Size(min = 4,max = 50, message = "Czas trwania ogłoszenia musi zawierać od 4 do 50 znaków")
    private String duration;

    public AdvertisementCreationDto() {
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
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public AdvertisementCreationDto(Advertisement advertisement) {
        this.id = advertisement.getId();
        this.title = advertisement.getTitle();
        this.description = advertisement.getDescription();
        this.price = advertisement.getPrice();
        this.duration = advertisement.getDuration();

    }
}

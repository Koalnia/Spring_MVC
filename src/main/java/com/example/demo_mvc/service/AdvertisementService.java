package com.example.demo_mvc.service;

import com.example.demo_mvc.dto.AdvertisementCreationDto;
import com.example.demo_mvc.model.Advertisement;
import com.example.demo_mvc.model.User;
import com.example.demo_mvc.repository.AdvertisementRepository;
import com.example.demo_mvc.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;


import java.security.Principal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class AdvertisementService {

    @Autowired
    AdvertisementRepository advertisementRepository;
    @Autowired
    UserRepository userRepository;


    public void addAdvertisement(AdvertisementCreationDto advertisement, Principal principal ) {

        User user = userRepository.findByEmail(principal.getName());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");
        String date = LocalDateTime.now().format(formatter);
        advertisementRepository.save(new Advertisement(user,
                advertisement.getTitle(),
                advertisement.getDescription(),
                advertisement.getPrice(),
                advertisement.getDuration(),
                date));

    }

    public void updateAdvertisement(Advertisement advertisement, AdvertisementCreationDto advertisementCreationDto) {
        advertisement.setTitle(advertisementCreationDto.getTitle());
        advertisement.setDescription(advertisementCreationDto.getDescription());
        advertisement.setPrice(advertisementCreationDto.getPrice());
        advertisement.setDuration(advertisementCreationDto.getDuration());
        advertisementRepository.save(advertisement);
    }

    public boolean isAdmin(Principal principal) {
        UserDetails userDetails = (UserDetails) ((Authentication) principal).getPrincipal();
        return userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ADMIN"));
    }

}

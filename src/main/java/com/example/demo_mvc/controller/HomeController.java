package com.example.demo_mvc.controller;

import com.example.demo_mvc.repository.AdvertisementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class HomeController {

    @Autowired
    AdvertisementRepository advertisementRepository;
    @GetMapping("/login")
    public String login() {
        return "login";
    }



}
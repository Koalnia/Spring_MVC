package com.example.demo_mvc.controller;

import com.example.demo_mvc.dto.AdvertisementCreationDto;
import com.example.demo_mvc.model.Advertisement;
import com.example.demo_mvc.repository.AdvertisementRepository;
import com.example.demo_mvc.repository.UserRepository;
import com.example.demo_mvc.service.AdvertisementService;
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
@RequestMapping("/advertisement")
public class AdvertisementController {
    @Autowired
    AdvertisementRepository advertisementRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    AdvertisementService advertisementService;


   @GetMapping("/index")
   public String showAdvertisementList(@RequestParam(value = "title", required = false) String title, Model model) {
       List<Advertisement> advertisements;

       if (title != null && !title.isEmpty()) {
           advertisements = advertisementRepository.findByTitleContainingIgnoreCase(title);
       } else {
           advertisements = advertisementRepository.findAll();
       }

       model.addAttribute("advertisements", advertisements.stream()
               .sorted(Comparator.comparing(Advertisement::getTitle))
               .collect(Collectors.toList()));
       return "index_advertisement";
   }

    @GetMapping("/addAdvertisement")
    public String showAdvertisementForm(Advertisement advertisement) {
        return "add-advertisement";
    }

    @PostMapping("/addAdvertisement")
    public String addAdvertisement(@ModelAttribute("new_advertisement") @Valid AdvertisementCreationDto advertisement, Errors errores, Principal principal ) {
        if(errores.hasErrors()){
            return "add-advertisement";
        }
        advertisementService.addAdvertisement(advertisement, principal);
        return "redirect:/advertisement/index";
    }

    @GetMapping("/update/{id}")
    public String showUpdateForm(@PathVariable("id") long id, Model model , Principal principal) {

        Advertisement advertisement = advertisementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid advertisement Id:" + id));
        if (!principal.getName().equals(advertisement.getUser().getEmail()) &&
                !advertisementService.isAdmin(principal)) {
            throw new AccessDeniedException("Nie masz dostępu do edycji tego ogłoszenia.");
        }
        AdvertisementCreationDto advertisementCreationDto = new AdvertisementCreationDto(advertisement);
        model.addAttribute("advertisement", advertisementCreationDto);
        return "update-advertisement";
    }
    @PostMapping("/update/{id}")
    public String updateAdvertisement(@PathVariable("id") long id, @ModelAttribute("advertisement") @Valid AdvertisementCreationDto advertisementCreationDto,
                                Errors errors, Principal principal) {
        if(errors.hasErrors()){
            return "update-advertisement";
        }
        Advertisement advertisement = advertisementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid advertisement Id:" + id));
        if (!principal.getName().equals(advertisement.getUser().getEmail()) &&
                !advertisementService.isAdmin(principal)) {
            throw new AccessDeniedException("Nie masz dostępu do edycji tego ogłoszenia.");
        }
        advertisementService.updateAdvertisement(advertisement, advertisementCreationDto);
        return "redirect:/advertisement/index";
    }

    @PostMapping("/delete/{id}")
    public String deleteAdvertisement(@PathVariable("id") long id, Model model, Principal principal) {

        Advertisement advertisement = advertisementRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid advertisement Id:" + id));
        if (!principal.getName().equals(advertisement.getUser().getEmail()) &&
                !advertisementService.isAdmin(principal)) {
            throw new AccessDeniedException("Nie masz dostępu do edycji tego ogłoszenia.");
        }
        advertisementRepository.delete(advertisement);
        return "redirect:/advertisement/index";
    }

    @ModelAttribute("new_advertisement")
    public AdvertisementCreationDto createAdvertisementDto() {
        return new AdvertisementCreationDto();
    }


}

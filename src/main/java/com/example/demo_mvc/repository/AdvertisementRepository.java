package com.example.demo_mvc.repository;

import com.example.demo_mvc.model.Advertisement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdvertisementRepository extends JpaRepository<Advertisement, Long> {


    List<Advertisement> findByTitleContainingIgnoreCase(String title);
}

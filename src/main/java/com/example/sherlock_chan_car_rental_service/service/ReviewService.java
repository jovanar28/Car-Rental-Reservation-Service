package com.example.sherlock_chan_car_rental_service.service;

import com.example.sherlock_chan_car_rental_service.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
public interface ReviewService {


    Page<ReviewDto> findAll(Pageable pageable);

    List<CompanyDto> getCompaniesByReview();
    ReviewDto findById(Long id);
    List<ReviewDto> findByCity(String city_name);
    List<ReviewDto> findByCompany(String company_name);

    ReviewDto createReview(ReviewCreateDto reviewCreateDto);

    ReviewDto updateReview(Long id, ReviewUpdateDto reviewUpdateDto);

    void deleteById(Long id);
}

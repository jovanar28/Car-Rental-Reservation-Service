package com.example.sherlock_chan_car_rental_service.service;

import com.example.sherlock_chan_car_rental_service.dto.CompanyCreateDto;
import com.example.sherlock_chan_car_rental_service.dto.CompanyDto;
import com.example.sherlock_chan_car_rental_service.dto.CompanyUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CompanyService {

    Page<CompanyDto> findAll(Pageable pageable);

    CompanyDto findById(Long id);

    CompanyDto updateCompany(Long id, CompanyUpdateDto companyUpdateDto);
    CompanyDto createCompany(CompanyCreateDto companyCreateDto);
    
    CompanyDto findCompanyByName(String name);
    
    CompanyDto deleteCompany(Long id);
}


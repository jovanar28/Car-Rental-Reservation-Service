package com.example.sherlock_chan_car_rental_service.service;

import com.example.sherlock_chan_car_rental_service.domain.Model;
import com.example.sherlock_chan_car_rental_service.dto.ModelCreateDto;
import com.example.sherlock_chan_car_rental_service.dto.ModelDto;
import com.example.sherlock_chan_car_rental_service.dto.ModelUpdateDto;
import org.springframework.boot.Banner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ModelService {

    Page<ModelDto> findAll(Pageable pageable);

    ModelDto findById(Long id);
    String findModelNameById(Long id);

    ModelDto createModel(ModelCreateDto modelCreateDto);

    ModelDto updateModel(Long id, ModelUpdateDto modelUpdateDto);

    void deleteById(Long id);

}

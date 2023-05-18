package com.example.sherlock_chan_car_rental_service.service;

import com.example.sherlock_chan_car_rental_service.domain.Vehicle;
import com.example.sherlock_chan_car_rental_service.dto.VehicleCreateDto;
import com.example.sherlock_chan_car_rental_service.dto.VehicleDto;
import com.example.sherlock_chan_car_rental_service.dto.VehicleUpdateDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehicleService {

    Page<VehicleDto> findAll(Pageable pageable);

    VehicleDto findById(Long id);
    List<VehicleDto> getVehiclesByType(String type_name);
    List<VehicleDto> getVehiclesByModel(String model_name);

    List<VehicleDto> getVehiclesByCompany(String company_name);

//    List<VehicleDto> getVehiclesByCompanyAndType(String company_name,String type_name);
//    List<VehicleDto> getVehiclesByCompanyAndModel(String company_name,String model_name);
    VehicleDto createVehicle(VehicleCreateDto vehicleCreateDto);


    VehicleDto updateVehicle(Long id, VehicleUpdateDto vehicleUpdateDto);

    void deleteById(Long id);


}

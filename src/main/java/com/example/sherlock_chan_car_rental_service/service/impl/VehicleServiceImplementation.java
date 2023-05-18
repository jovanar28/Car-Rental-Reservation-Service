package com.example.sherlock_chan_car_rental_service.service.impl;

import com.example.sherlock_chan_car_rental_service.domain.Vehicle;
import com.example.sherlock_chan_car_rental_service.dto.TypeDto;
import com.example.sherlock_chan_car_rental_service.dto.VehicleCreateDto;
import com.example.sherlock_chan_car_rental_service.dto.VehicleDto;
import com.example.sherlock_chan_car_rental_service.dto.VehicleUpdateDto;
import com.example.sherlock_chan_car_rental_service.exception.NotFoundException;
import com.example.sherlock_chan_car_rental_service.mapper.ModelMapper;
import com.example.sherlock_chan_car_rental_service.mapper.TypeMapper;
import com.example.sherlock_chan_car_rental_service.mapper.VehicleMapper;
import com.example.sherlock_chan_car_rental_service.repository.ModelRepository;
import com.example.sherlock_chan_car_rental_service.repository.TypeRepository;
import com.example.sherlock_chan_car_rental_service.repository.VehicleRepository;
import com.example.sherlock_chan_car_rental_service.service.VehicleService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service

public class VehicleServiceImplementation implements VehicleService {

    private VehicleRepository vehicleRepository;
    private VehicleMapper vehicleMapper;

    private TypeRepository typeRepository;
    private TypeMapper typeMapper;
    private ModelRepository modelRepository;
    private ModelMapper modelMapper;

    public VehicleServiceImplementation(VehicleRepository vehicleRepository, VehicleMapper vehicleMapper, TypeRepository typeRepository, TypeMapper typeMapper, ModelRepository modelRepository, ModelMapper modelMapper) {
        this.vehicleRepository = vehicleRepository;
        this.vehicleMapper = vehicleMapper;
        this.typeRepository = typeRepository;
        this.typeMapper = typeMapper;
        this.modelRepository = modelRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<VehicleDto> findAll(Pageable pageable) {
        return (Page<VehicleDto>) vehicleRepository.findAll(pageable)
                .map(vehicleMapper::vehicleToVehicleDto);
    }

    @Override
    public VehicleDto findById(Long id) {
        return vehicleRepository.findById(id)
                .map(vehicleMapper::vehicleToVehicleDto)
                .orElseThrow(()->
                    new NotFoundException(String.format("Vehicle with id %d doesn't exist",id))
                );
    }

    @Override
    public List<VehicleDto> getVehiclesByType(String type_name) {
        return vehicleRepository.findAll().stream()
                .filter(vehicle -> vehicle.getType().getName().equals(type_name))
                .map(vehicleMapper::vehicleToVehicleDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleDto> getVehiclesByModel(String model_name) {
        return vehicleRepository.findAll().stream()
                .filter(vehicle -> vehicle.getModel().getName().equals(model_name))
                .map(vehicleMapper::vehicleToVehicleDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<VehicleDto> getVehiclesByCompany(String company_name) {
        return vehicleRepository.findAll().stream()
                .filter(vehicle -> vehicle.getCompany().getName().equals(company_name))
                .map(vehicleMapper::vehicleToVehicleDto)
                .collect(Collectors.toList());
    }



    @Override
    public VehicleDto createVehicle(VehicleCreateDto vehicleCreateDto) {
        Vehicle vehicle=vehicleMapper.vehicleCreateDtoToVehicle(vehicleCreateDto);
        vehicleRepository.save(vehicle);
        return vehicleMapper.vehicleToVehicleDto(vehicle);
    }

    @Override
    public VehicleDto updateVehicle(Long id, VehicleUpdateDto vehicleUpdateDto) {
        Vehicle vehicle=vehicleRepository.findById(id)
                .orElseThrow(()->new NotFoundException(String.format("Vehicle with id: %d not found",id)));
        vehicle.setModel(modelMapper.modelDtoToModel(vehicleUpdateDto.getModelDto()));
        vehicle.setType(typeMapper.typeDtoToType(vehicleUpdateDto.getTypeDto()));
        return vehicleMapper.vehicleToVehicleDto(vehicle);
    }

    @Override
    public void deleteById(Long id) {
        vehicleRepository.deleteById(id);

    }
}

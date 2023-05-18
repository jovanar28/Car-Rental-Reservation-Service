package com.example.sherlock_chan_car_rental_service.mapper;

import com.example.sherlock_chan_car_rental_service.domain.Vehicle;
import com.example.sherlock_chan_car_rental_service.dto.VehicleCreateDto;
import com.example.sherlock_chan_car_rental_service.dto.VehicleDto;
import com.example.sherlock_chan_car_rental_service.exception.NotFoundException;
import com.example.sherlock_chan_car_rental_service.repository.CompanyRepository;
import com.example.sherlock_chan_car_rental_service.repository.ModelRepository;
import com.example.sherlock_chan_car_rental_service.repository.TypeRepository;
import org.springframework.stereotype.Component;

@Component
public class VehicleMapper {

    private ModelRepository modelRepository;
    private TypeRepository typeRepository;
    private ModelMapper modelMapper;
    private TypeMapper typeMapper;

    private CompanyMapper companyMapper;
    private CompanyRepository companyRepository;


    public VehicleMapper(ModelRepository modelRepository, TypeRepository typeRepository,
                         ModelMapper modelMapper, TypeMapper typeMapper,
                         CompanyMapper companyMapper,CompanyRepository companyRepository){
        this.modelMapper = modelMapper;
        this.typeMapper = typeMapper;
        this.modelRepository = modelRepository;
        this.typeRepository = typeRepository;
        this.companyMapper=companyMapper;
        this.companyRepository=companyRepository;
    }

    public VehicleDto vehicleToVehicleDto(Vehicle vehicle){
        VehicleDto vehicleDto = new VehicleDto();
        vehicleDto.setId(vehicle.getId());
        vehicleDto.setModelDto(modelMapper.modelToModelDto(vehicle.getModel()));
        vehicleDto.setTypeDto(typeMapper.typeToTypeDto(vehicle.getType()));
        vehicleDto.setCompanyDto(companyMapper.companyToCompanyDto(vehicle.getCompany()));

        return vehicleDto;
    }

    public Vehicle vehicleCreateDtoToVehicle(VehicleCreateDto vehicleCreateDto){
        Vehicle vehicle = new Vehicle();
        vehicle.setModel(modelRepository
                .findById(vehicleCreateDto.getModel_id())
                .orElseThrow(() -> new NotFoundException(String.format("Model with provided id %d has not been found", vehicleCreateDto.getModel_id()))));
        vehicle.setType(typeRepository.
                findById(vehicleCreateDto.getType_id())
                .orElseThrow(() -> new NotFoundException(String.format("Type with provided id %d has not been found", vehicleCreateDto.getType_id()))));
        vehicle.setCompany(companyRepository.
                findById(vehicleCreateDto.getCompany_id())
                .orElseThrow(()->new NotFoundException(String.format("Company with provided id %d has not been found",vehicleCreateDto.getCompany_id()))));
        return vehicle;
    }

    public Vehicle vehicleDtoToVehicle(VehicleDto vehicleDto){
        Vehicle vehicle = new Vehicle();
        vehicle.setId(vehicleDto.getId());
        vehicle.setType(typeMapper.typeDtoToType(vehicleDto.getTypeDto()));
        vehicle.setModel(modelMapper.modelDtoToModel(vehicleDto.getModelDto()));
        vehicle.setCompany(companyMapper.companyDtoToCompany(vehicleDto.getCompanyDto()));
        return vehicle;
    }
}

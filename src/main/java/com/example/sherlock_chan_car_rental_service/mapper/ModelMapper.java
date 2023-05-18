package com.example.sherlock_chan_car_rental_service.mapper;

import com.example.sherlock_chan_car_rental_service.domain.Model;
import com.example.sherlock_chan_car_rental_service.dto.ModelCreateDto;
import com.example.sherlock_chan_car_rental_service.dto.ModelDto;
import com.example.sherlock_chan_car_rental_service.exception.NotFoundException;
import com.example.sherlock_chan_car_rental_service.repository.VehicleRepository;
import org.springframework.stereotype.Component;

@Component
public class ModelMapper {

    public ModelDto modelToModelDto(Model model){
        ModelDto modelDto=new ModelDto();
        modelDto.setId(model.getId());
        modelDto.setName(model.getName());
        modelDto.setPrice(model.getPrice());
        return modelDto;
    }
    public Model modelDtoToModel(ModelDto modelDto){
        Model model=new Model();
        model.setId(modelDto.getId());
        model.setName(modelDto.getName());
        model.setPrice(modelDto.getPrice());
        return model;
    }

    public Model modelCreateDtoToModel(ModelCreateDto modelCreateDto){
        Model model=new Model();
        model.setName(modelCreateDto.getName());
        model.setPrice(modelCreateDto.getPrice());

        return model;
    }
}

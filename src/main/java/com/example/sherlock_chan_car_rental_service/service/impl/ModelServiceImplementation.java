package com.example.sherlock_chan_car_rental_service.service.impl;

import com.example.sherlock_chan_car_rental_service.domain.Model;
import com.example.sherlock_chan_car_rental_service.dto.ModelCreateDto;
import com.example.sherlock_chan_car_rental_service.dto.ModelDto;
import com.example.sherlock_chan_car_rental_service.dto.ModelUpdateDto;
import com.example.sherlock_chan_car_rental_service.exception.NotFoundException;
import com.example.sherlock_chan_car_rental_service.mapper.ModelMapper;
import com.example.sherlock_chan_car_rental_service.repository.ModelRepository;
import com.example.sherlock_chan_car_rental_service.service.ModelService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ModelServiceImplementation implements ModelService {
    private ModelRepository modelRepository;
    private ModelMapper modelMapper;

    public ModelServiceImplementation(ModelRepository modelRepository, ModelMapper modelMapper) {
        this.modelRepository = modelRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public Page<ModelDto> findAll(Pageable pageable) {
        return modelRepository.findAll(pageable)
                .map(modelMapper::modelToModelDto);
    }

    @Override
    public ModelDto findById(Long id) {
        return modelRepository.findById(id)
                .map(modelMapper::modelToModelDto)
                .orElseThrow(()->
                        new NotFoundException(String.format("Model with id %d doesnt exist",id)));
    }

    @Override
    public String findModelNameById(Long id) {
        Model model=modelRepository
                .findById(id)
                .orElseThrow(()->
                        new NotFoundException(String.format("Model with provided id %d isn't found",id)));
        return model.getName();
    }


    @Override
    public ModelDto createModel(ModelCreateDto modelCreateDto) {
        Model model=modelMapper.modelCreateDtoToModel(modelCreateDto);
        modelRepository.save(model);
        return modelMapper.modelToModelDto(model);
    }

    @Override
    public ModelDto updateModel(Long id, ModelUpdateDto modelUpdateDto) {
        Model model=modelRepository.findById(id)
                .orElseThrow(()->new NotFoundException(String.format("Model with id: %d not found.", id)));
        model.setName(modelUpdateDto.getName());
        model.setPrice(modelUpdateDto.getPrice());
        return modelMapper.modelToModelDto(modelRepository.save(model));
    }

    @Override
    public void deleteById(Long id) {
        modelRepository.deleteById(id);
    }

}

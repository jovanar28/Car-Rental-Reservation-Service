package com.example.sherlock_chan_car_rental_service.service.impl;

import com.example.sherlock_chan_car_rental_service.domain.Type;
import com.example.sherlock_chan_car_rental_service.dto.TypeCreateDto;
import com.example.sherlock_chan_car_rental_service.dto.TypeDto;
import com.example.sherlock_chan_car_rental_service.dto.TypeUpdateDto;
import com.example.sherlock_chan_car_rental_service.exception.NotFoundException;
import com.example.sherlock_chan_car_rental_service.mapper.TypeMapper;
import com.example.sherlock_chan_car_rental_service.repository.TypeRepository;
import com.example.sherlock_chan_car_rental_service.service.TypeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service

public class TypeServiceImplementation implements TypeService {

    private TypeRepository typeRepository;
    private TypeMapper typeMapper;

    public TypeServiceImplementation(TypeMapper typeMapper, TypeRepository typeRepository){
        this.typeRepository = typeRepository;
        this.typeMapper = typeMapper;
    }

    @Override
    public Page<TypeDto> findAll(Pageable pageable) {
        return (Page<TypeDto>) typeRepository.findAll(pageable)
                .map(typeMapper::typeToTypeDto);
    }

    @Override
    public TypeDto findById(Long id) {
        return typeRepository.findById(id)
                .map(typeMapper::typeToTypeDto)
                .orElseThrow(()->
                        new NotFoundException(String.format("Type with id %d doesn't exist",id)));
    }

    @Override
    public String findTypeNameById(Long id) {
        Type type = typeRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Type with provided id %d has not been found", id)
                ));

        return type.getName();
    }

    @Override
    public TypeDto createType(TypeCreateDto typeCreateDto) {
        Type type = typeMapper.typeCreateDtoToType(typeCreateDto);
        typeRepository.save(type);

        return typeMapper.typeToTypeDto(type);
    }

    @Override
    public TypeDto updateType(Long id, TypeUpdateDto typeUpdateDto) {
        Type type = typeRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Type with provided id %d has not been found", id)
                ));

        type.setName(typeUpdateDto.getName());

        return typeMapper.typeToTypeDto(typeRepository.save(type));
    }

    @Override
    public void deleteById(Long id) {
        typeRepository.deleteById(id);

    }
}

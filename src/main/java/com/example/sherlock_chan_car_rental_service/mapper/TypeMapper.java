package com.example.sherlock_chan_car_rental_service.mapper;

import com.example.sherlock_chan_car_rental_service.domain.Type;
import com.example.sherlock_chan_car_rental_service.domain.Vehicle;
import com.example.sherlock_chan_car_rental_service.dto.TypeCreateDto;
import com.example.sherlock_chan_car_rental_service.dto.TypeDto;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TypeMapper {

    public TypeDto typeToTypeDto(Type type){
        TypeDto typeDto=new TypeDto();
        typeDto.setId(type.getId());
        typeDto.setName(type.getName());
        return typeDto;
    }

    public Type typeDtoToType(TypeDto typeDto){
        Type type=new Type();
        type.setId(typeDto.getId());
        type.setName(typeDto.getName());
        return type;
    }
    public Type typeCreateDtoToType(TypeCreateDto typeCreateDto){
        Type type=new Type();
        type.setName(typeCreateDto.getName());
//        type.setVehicles(vehicles);
        return type;
    }
}

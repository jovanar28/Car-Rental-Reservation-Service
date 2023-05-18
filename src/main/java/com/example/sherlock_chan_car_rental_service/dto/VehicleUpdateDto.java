package com.example.sherlock_chan_car_rental_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class VehicleUpdateDto {

    private Long id;

    @JsonProperty("model")
    private ModelDto modelDto;

    @JsonProperty("type")
    private TypeDto typeDto;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public ModelDto getModelDto() {
        return modelDto;
    }

    public void setModelDto(ModelDto modelDto) {
        this.modelDto = modelDto;
    }

    public TypeDto getTypeDto() {
        return typeDto;
    }

    public void setTypeDto(TypeDto typeDto) {
        this.typeDto = typeDto;
    }
}

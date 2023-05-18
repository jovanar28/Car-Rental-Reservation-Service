package com.example.sherlock_chan_car_rental_service.dto;


import com.fasterxml.jackson.annotation.JsonProperty;

public class VehicleDto {

    private Long id;

    @JsonProperty("model")
    private ModelDto modelDto;

    @JsonProperty("type")
    private TypeDto typeDto;

    @JsonProperty("company")
    private CompanyDto companyDto;

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

    public CompanyDto getCompanyDto() {
        return companyDto;
    }

    public void setCompanyDto(CompanyDto companyDto) {
        this.companyDto = companyDto;
    }
}

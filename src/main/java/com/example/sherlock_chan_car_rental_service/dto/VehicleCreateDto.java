package com.example.sherlock_chan_car_rental_service.dto;

import javax.validation.constraints.NotNull;

public class VehicleCreateDto {

    @NotNull
    private Long model_id;

    @NotNull
    private Long type_id;

    @NotNull
    private Long company_id;

    public Long getModel_id() {
        return model_id;
    }

    public void setModel_id(Long model_id) {
        this.model_id = model_id;
    }

    public Long getType_id() {
        return type_id;
    }

    public void setType_id(Long type_id) {
        this.type_id = type_id;
    }

    public Long getCompany_id() {
        return company_id;
    }

    public void setCompany_id(Long company_id) {
        this.company_id = company_id;
    }
}

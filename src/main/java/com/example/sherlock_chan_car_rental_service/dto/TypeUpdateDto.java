package com.example.sherlock_chan_car_rental_service.dto;

import javax.validation.constraints.NotEmpty;

public class TypeUpdateDto {

    @NotEmpty(message = "Text can't be empty")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

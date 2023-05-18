package com.example.sherlock_chan_car_rental_service.dto;

import javax.validation.constraints.NotEmpty;

public class TypeDto {
    private Long id;
    @NotEmpty(message = "Text cant be empty")
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

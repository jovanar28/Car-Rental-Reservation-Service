package com.example.sherlock_chan_car_rental_service.user_service.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DiscountDto {
    @JsonProperty("discount")
    private Integer discount;

    public DiscountDto(){

    }

    public DiscountDto(Integer discount){
        this.discount = discount;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }
}

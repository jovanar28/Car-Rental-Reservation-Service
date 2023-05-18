package com.example.sherlock_chan_car_rental_service.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class CompanyUpdateDto {

    @NotEmpty(message = "Text can't be empty")
    private String name;

    @NotEmpty(message = "Text can't be empty")
    private String description;

    @NotNull
    @JsonProperty("numVehicles")
    private Integer numVehicles;

    @NotNull
    private AddressDto addressDto;

    public static class AddressDto{
        @NotEmpty(message = "Text can't be empty")
        private String country;
        @NotEmpty(message = "Text can't be empty")
        private String city;
        @NotEmpty(message = "Text can't be empty")
        private String postcode;
        @NotEmpty(message = "Text can't be empty")
        private String street;

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getPostcode() {
            return postcode;
        }

        public void setPostcode(String postcode) {
            this.postcode = postcode;
        }

        public String getStreet() {
            return street;
        }

        public void setStreet(String street) {
            this.street = street;
        }
    }

    public AddressDto getAddressDto() {
        return addressDto;
    }

    public void setAddressDto(AddressDto addressDto) {
        this.addressDto = addressDto;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNumVehicles() {
        return numVehicles;
    }

    public void setNumVehicles(Integer numVehicles) {
        this.numVehicles = numVehicles;
    }
}

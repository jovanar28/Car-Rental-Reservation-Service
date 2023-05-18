package com.example.sherlock_chan_car_rental_service.domain;


import javax.persistence.*;

@Embeddable
public class Address {

    private String country;
    private String city;
    private String postcode;
    private String street;

    public Address(){

    }
    public Address(String country, String city, String postcode, String street) {
        this.country = country;
        this.city = city;
        this.postcode = postcode;
        this.street = street;
    }


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

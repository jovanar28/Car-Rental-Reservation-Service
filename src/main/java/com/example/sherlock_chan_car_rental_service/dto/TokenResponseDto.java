package com.example.sherlock_chan_car_rental_service.dto;

public class TokenResponseDto {

    private String token;

    public TokenResponseDto(){

    }

    public TokenResponseDto(String token){
        this.token=token;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}

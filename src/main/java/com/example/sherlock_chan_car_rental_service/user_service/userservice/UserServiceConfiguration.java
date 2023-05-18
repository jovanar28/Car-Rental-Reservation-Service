package com.example.sherlock_chan_car_rental_service.user_service.userservice;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Configuration
public class UserServiceConfiguration {

    @Bean
    public RestTemplate userServiceRestTemplate(){
        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory("http://localhost:31337/api"));
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory("http://localhost:1312/rental-user-service/api"));
        return restTemplate;
    }
}

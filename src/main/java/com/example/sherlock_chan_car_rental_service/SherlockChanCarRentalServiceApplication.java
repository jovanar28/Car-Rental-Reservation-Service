package com.example.sherlock_chan_car_rental_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class SherlockChanCarRentalServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SherlockChanCarRentalServiceApplication.class, args);
	}

}

package com.example.sherlock_chan_car_rental_service.repository;

import com.example.sherlock_chan_car_rental_service.domain.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VehicleRepository extends JpaRepository<Vehicle, Long> {
}

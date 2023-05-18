package com.example.sherlock_chan_car_rental_service.repository;

import com.example.sherlock_chan_car_rental_service.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}

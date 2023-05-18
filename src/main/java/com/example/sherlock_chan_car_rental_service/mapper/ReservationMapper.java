package com.example.sherlock_chan_car_rental_service.mapper;

import com.example.sherlock_chan_car_rental_service.domain.Company;
import com.example.sherlock_chan_car_rental_service.domain.Reservation;
import com.example.sherlock_chan_car_rental_service.domain.Vehicle;
import com.example.sherlock_chan_car_rental_service.dto.ReservationCreateDto;
import com.example.sherlock_chan_car_rental_service.dto.ReservationDto;
import com.example.sherlock_chan_car_rental_service.exception.NotFoundException;
import com.example.sherlock_chan_car_rental_service.repository.CompanyRepository;
import com.example.sherlock_chan_car_rental_service.repository.VehicleRepository;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    private CompanyMapper companyMapper;
    private VehicleMapper vehicleMapper;
    private CompanyRepository companyRepository;
    private VehicleRepository vehicleRepository;

    public ReservationMapper(CompanyRepository companyRepository, VehicleRepository vehicleRepository
            , CompanyMapper companyMapper, VehicleMapper vehicleMapper){
        this.companyMapper = companyMapper;
        this.vehicleMapper = vehicleMapper;
        this.companyRepository = companyRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public ReservationDto reservationToReservationDto(Reservation reservation){
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setId(reservation.getId());
        reservationDto.setVehicleDto(vehicleMapper.vehicleToVehicleDto(reservation.getVehicle()));
        reservationDto.setCompanyDto(companyMapper.companyToCompanyDto(reservation.getCompany()));
        reservationDto.setIs_active(reservation.getIs_active());
        reservationDto.setUser_id(reservation.getUser_id());
        reservationDto.setTotal_price(reservation.getTotal_price());
        reservationDto.setStarting_date(reservation.getStarting_date());
        reservationDto.setEnding_date(reservation.getEnding_date());

        return reservationDto;
    }

    public Reservation reservationCreateDtoToReservation(ReservationCreateDto reservationCreateDto){
        Reservation reservation = new Reservation();

        Vehicle vehicle = vehicleRepository
                .findById(reservationCreateDto.getVehicle_id())
                .orElseThrow(() -> new NotFoundException(String.format("Vehicle with provided id %d cannot be found", reservationCreateDto.getVehicle_id())));

        Company company = vehicle.getCompany();

        reservation.setEnding_date(reservationCreateDto.getEnding_date());
        reservation.setStarting_date(reservationCreateDto.getStarting_date());
        reservation.setUser_id(reservationCreateDto.getUser_id());
        reservation.setVehicle(vehicle);
        reservation.setCompany(company);

        return reservation;
    }
}

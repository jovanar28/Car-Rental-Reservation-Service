package com.example.sherlock_chan_car_rental_service.controller;

import com.example.sherlock_chan_car_rental_service.dto.*;
import com.example.sherlock_chan_car_rental_service.security.CheckSecurity;
import com.example.sherlock_chan_car_rental_service.service.ReservationService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
@RestController
@RequestMapping("/schedule")

public class ReservationController {
    private ReservationService reservationService;

    public ReservationController(ReservationService reservationService){
        this.reservationService = reservationService;
    }

    @ApiOperation(value = "Get all schedules")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "What page number you want", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "size", value = "Number of items to return", dataType = "string", paramType = "query"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). " +
                            "Default sort order is ascending. " +
                            "Multiple sort criteria are supported.")})
    @GetMapping
    @CheckSecurity(userTypes = {"Admin","Manager","Client"})
    public ResponseEntity<Page<ReservationDto>> getAllSchedules(@RequestHeader("Authorization") String authorization,
                                                                @ApiIgnore Pageable pageable){
        return new ResponseEntity<>(reservationService.findAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/listByCity/{city}/{sort}")
    @CheckSecurity(userTypes = {"Admin","Manager","Client"})
    public ResponseEntity<List<ReservationDto>> findByCity(@RequestHeader("Authorization") String authorization,
                                                           @PathVariable("city") String city, @PathVariable("sort") boolean sort){
        return new ResponseEntity<>(reservationService.findByCity(city, sort),HttpStatus.OK);
    }

    @GetMapping("/listByCompany/{company}/{sort}")
    @CheckSecurity(userTypes = {"Admin","Manager","Client"})
    public ResponseEntity<List<ReservationDto>> findByCompany(@RequestHeader("Authorization") String authorization,
                                                              @PathVariable("company") String company, @PathVariable("sort") boolean sort){
        return new ResponseEntity<>(reservationService.findByCompany(company, sort),HttpStatus.OK);
    }

    @GetMapping("/listByDate/{start_date}/{end_date}/{sort}")
    @CheckSecurity(userTypes = {"Admin","Manager","Client"})
    public ResponseEntity<List<ReservationDto>> findByDate(@RequestHeader("Authorization") String authorization,
                                                           @PathVariable("start_date")String start_date,
                                                           @PathVariable("end_date") String end_date, @PathVariable("sort") boolean sort){

        LocalDate start_local_date = null;
        LocalDate end_local_date = null;

        try{
            start_local_date = LocalDate.parse(start_date);
            end_local_date = LocalDate.parse(end_date);
        }catch (DateTimeParseException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        System.out.printf("%s %s",start_date,end_date);
        return new ResponseEntity<>(reservationService.findByDate(start_local_date,end_local_date, sort),HttpStatus.OK);
    }

    @GetMapping("/listByAll/{vehicle_type}/{city_name}/{company_name}/{start_date}/{end_date}/{sort}")
    @CheckSecurity(userTypes = {"Admin","Manager","Client"})
    public ResponseEntity<List<ReservationDto>> findByAllParams(@RequestHeader("Authorization") String authorization,
                                                                @PathVariable("vehicle_type") String vehicle_type,
                                                                @PathVariable("city_name") String city_name,
                                                                @PathVariable("company_name") String company_name,
                                                                @PathVariable("start_date") String start_date,
                                                                @PathVariable("end_date") String end_date,
                                                                @PathVariable("sort") boolean sort){
        LocalDate start_date_local = null;
        LocalDate end_date_local = null;

        try{
            start_date_local = LocalDate.parse(start_date);
            end_date_local = LocalDate.parse(end_date);
        }catch (DateTimeParseException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(reservationService.filterByAll(vehicle_type,city_name,company_name,start_date_local,end_date_local,sort), HttpStatus.OK);
    }

    @GetMapping("/getAvailableVehicles/{start_date}/{end_date}")
    @CheckSecurity(userTypes = {"Admin","Manager","Client"})
    public ResponseEntity<List<VehicleDto>> getAvailableVehicles(@RequestHeader("Authorization") String authorization,
                                                                 @PathVariable("start_date") String start_date,
                                                                 @PathVariable("end_date") String end_date){
        LocalDate l_start_date = null;
        LocalDate l_end_date = null;

        try{
            l_start_date = LocalDate.parse(start_date);
            l_end_date = LocalDate.parse(end_date);
        }catch (DateTimeParseException e){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(reservationService.listAvailableVehiclesNew(l_start_date, l_start_date), HttpStatus.OK);
    }

    @PostMapping("/reserveByType/")
    @CheckSecurity(userTypes = {"Client"})
    public ResponseEntity<ReservationDto> reserveByType(@RequestHeader("Authorization") String authorization,
                                                        @RequestBody @Valid ReservationCreateByTypeDto reservationCreateByTypeDto){
        return new ResponseEntity<>(reservationService.createReservationByType(reservationCreateByTypeDto), HttpStatus.OK);
    }

    @PostMapping("/reserveByModel/")
    @CheckSecurity(userTypes = {"Client"})
    public ResponseEntity<ReservationDto> reserveByModel(@RequestHeader("Authorization") String authorization,
                                                         @RequestBody @Valid ReservationCreateByModelDto reservationCreateByModelDto){
        return new ResponseEntity<>(reservationService.createReservationByModel(reservationCreateByModelDto), HttpStatus.OK);
    }

    @PostMapping("/reserveVehicle")
    @CheckSecurity(userTypes = {"Client"})
    public ResponseEntity<ReservationDto> reserveVehicle(@RequestHeader("Authorization") String authorization,
                                                         @RequestBody @Valid ReservationCreateDto reservationCreateDto){
        return new ResponseEntity<>(reservationService.reserveVehicle(reservationCreateDto), HttpStatus.OK);
    }

    @DeleteMapping("/cancelReservation/{id}")
    @CheckSecurity(userTypes = {"Manager","Client"})
    public ResponseEntity<ReservationDto> cancelReservation(@RequestHeader("Authorization") String authorization,
                                                            @PathVariable("id") Long reservation_id){
        return new ResponseEntity<>(reservationService.cancelReservation(reservation_id), HttpStatus.OK);
    }
}

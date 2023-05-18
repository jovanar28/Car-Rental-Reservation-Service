package com.example.sherlock_chan_car_rental_service.dto;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

public class ReservationCreateByModelDto {

    @NotNull
    private LocalDate starting_date;
    @NotNull
    private LocalDate ending_date;

    @NotNull
    private Long user_id;

    @NotNull
    private Long model_id;

    @NotNull
    private Long company_id;

    public LocalDate getStarting_date() {
        return starting_date;
    }

    public void setStarting_date(LocalDate starting_date) {
        this.starting_date = starting_date;
    }

    public LocalDate getEnding_date() {
        return ending_date;
    }

    public void setEnding_date(LocalDate ending_date) {
        this.ending_date = ending_date;
    }

    public Long getUser_id() {
        return user_id;
    }

    public void setUser_id(Long user_id) {
        this.user_id = user_id;
    }

    public Long getModel_id() {
        return model_id;
    }

    public void setModel_id(Long model_id) {
        this.model_id = model_id;
    }

    public Long getCompany_id() {
        return company_id;
    }

    public void setCompany_id(Long company_id) {
        this.company_id = company_id;
    }
}

package com.example.sherlock_chan_car_rental_service.dto.mailDtos;

import java.time.LocalDate;

public class ReservationMailDto {
    private String firstName;
    private String last_name;
    private String vehicle;
    private String company_name;
    private String company_manager_name;
    private String company_manager_lastname;
    private String company_manager_email;
    private String email;
    private LocalDate start_date;
    private LocalDate end_date;
    private Double total_price;
    private Integer discount;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public LocalDate getStart_date() {
        return start_date;
    }

    public void setStart_date(LocalDate start_date) {
        this.start_date = start_date;
    }

    public LocalDate getEnd_date() {
        return end_date;
    }

    public void setEnd_date(LocalDate end_date) {
        this.end_date = end_date;
    }

    public Double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(Double total_price) {
        this.total_price = total_price;
    }

    public Integer getDiscount() {
        return discount;
    }

    public void setDiscount(Integer discount) {
        this.discount = discount;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCompany_manager_name() {
        return company_manager_name;
    }

    public void setCompany_manager_name(String company_manager_name) {
        this.company_manager_name = company_manager_name;
    }

    public String getCompany_manager_lastname() {
        return company_manager_lastname;
    }

    public void setCompany_manager_lastname(String company_manager_lastname) {
        this.company_manager_lastname = company_manager_lastname;
    }

    public String getCompany_manager_email() {
        return company_manager_email;
    }

    public void setCompany_manager_email(String company_manager_email) {
        this.company_manager_email = company_manager_email;
    }
}

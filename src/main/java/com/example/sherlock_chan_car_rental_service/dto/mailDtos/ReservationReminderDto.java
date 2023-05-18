package com.example.sherlock_chan_car_rental_service.dto.mailDtos;

public class ReservationReminderDto {
    private String customerName;
    private String customerEmail;
    private String vehicleToReserve;
    private Integer daysLeft;

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerEmail() {
        return customerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        this.customerEmail = customerEmail;
    }

    public String getVehicleToReserve() {
        return vehicleToReserve;
    }

    public void setVehicleToReserve(String vehicleToReserve) {
        this.vehicleToReserve = vehicleToReserve;
    }

    public Integer getDaysLeft() {
        return daysLeft;
    }

    public void setDaysLeft(Integer daysLeft) {
        this.daysLeft = daysLeft;
    }
}

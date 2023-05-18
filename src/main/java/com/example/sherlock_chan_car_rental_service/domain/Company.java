package com.example.sherlock_chan_car_rental_service.domain;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "COMPANY")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    private Integer numVehicls;

    private Double rank;
    @Embedded
    private Address address;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "company", orphanRemoval = true)
    private List<Vehicle> vehicles = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL,  mappedBy = "company", orphanRemoval = true)
    private List<Review> reviews= new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "company", orphanRemoval = true)
    private List<Reservation> reservations = new ArrayList<>();

    public Company(){

    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getNumVehicls() {
        return numVehicls;
    }

    public void setNumVehicls(Integer numVehicls) {
        this.numVehicls = numVehicls;
    }

    public Double getRank() {
        return rank;
    }

    public void setRank(Double rank) {
        this.rank = rank;
    }

    @Override
    public boolean equals(Object obj) {
        if((obj!=null) && obj instanceof Company){
            Company company=(Company) obj;
            return this.getName().equals(company.getName());
        }
        return false;
    }
}

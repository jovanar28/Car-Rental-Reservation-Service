package com.example.sherlock_chan_car_rental_service.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Company company;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "vehicle",orphanRemoval = true)
    private List<Reservation> reservations =new ArrayList<>();

    @ManyToOne
    private Model model;

    @ManyToOne(cascade = CascadeType.ALL)
    private Type type;

    public Vehicle(){

    }
    public Vehicle(Long id, Company company, Model model, Type type) {
        this.id = id;
        this.company = company;
        this.model = model;
        this.type = type;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Model getModel() {
        return model;
    }

    public void setModel(Model model) {
        this.model = model;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj instanceof Vehicle){
            Vehicle vehicle = (Vehicle) obj;
            return this.getType().equals(vehicle.getType()) && this.getCompany().equals(vehicle.getCompany())
                    && this.getModel().equals(vehicle.getModel());
        }
        return false;
    }
}

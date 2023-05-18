package com.example.sherlock_chan_car_rental_service.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name="MODEL")
public class Model {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    private String name;

    private Double price;

    @OneToMany(cascade = CascadeType.REMOVE,mappedBy = "model",orphanRemoval = true)
    private List<Vehicle>vehicleList;


    public Model(){

    }

    public Model(Long id, String name,Double price){
        this.id=id;
        this.name=name;
        this.price=price;
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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj instanceof Model){
            Model model = (Model) obj;
            return this.getName().equalsIgnoreCase(model.getName());
        }
        return false;
    }
}

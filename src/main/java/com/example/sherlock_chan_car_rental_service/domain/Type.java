package com.example.sherlock_chan_car_rental_service.domain;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name="TYPE")
public class Type {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    private String name;

    @OneToMany(cascade = CascadeType.REMOVE,mappedBy = "type",orphanRemoval = true)
    private List<Vehicle>vehicleList;

    public Type(){

    }
    public Type(Long id, String name) {
        this.id = id;
        this.name = name;
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

    @Override
    public boolean equals(Object obj) {
        if(obj != null && obj instanceof Type){
            Type type = (Type) obj;
            return this.getName().equalsIgnoreCase(type.getName());
        }
        return false;
    }
}

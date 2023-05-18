package com.example.sherlock_chan_car_rental_service.domain;

import javax.persistence.*;

@Entity
@Table(name="REVIEW")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Integer star;
    private String comment;

    @ManyToOne
    private Company company;

    public Review(){

    }

    public Review(Long id, Integer star, String comment) {
        this.id = id;
        this.star = star;
        this.comment = comment;
        this.company = company;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getStar() {
        return star;
    }

    public void setStar(Integer star) {
        this.star = star;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }
}

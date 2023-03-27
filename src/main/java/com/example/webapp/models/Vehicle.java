package com.example.webapp.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

    @Entity
    @Getter
    @Setter
    @NoArgsConstructor
    public class Vehicle {
        @Id
        @GeneratedValue(strategy=GenerationType.IDENTITY)
        private long id;
        @NotBlank
        protected String brand;
        @NotBlank
        protected String model;
        @NotBlank
        protected String regNumber;
        @Min(1)
        @Max(99)
        protected int numberOfSeats;
        @ManyToMany(cascade = CascadeType.MERGE)
        @JoinTable(
                name = "Vehicle_Trip",
                joinColumns = {@JoinColumn(name="vehicle_id")},
                inverseJoinColumns = {@JoinColumn(name = "trip_id")}
        )
        protected List<Trip> tripList = new ArrayList<>();


    public Vehicle(String brand, String model, String regNumber, int numberOfSeats) {
        this.brand = brand;
        this.model = model;
        this.regNumber = regNumber;
        this.numberOfSeats = numberOfSeats;
    }
    public void addOrder(Trip trip){
        tripList.add(trip);
    }

    @Override
    public String toString() {
        return numberOfSeats + " " + brand + " " + model + " " + regNumber +  " ";
    }
}

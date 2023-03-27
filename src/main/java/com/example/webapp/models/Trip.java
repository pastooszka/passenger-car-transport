package com.example.webapp.models;

import com.example.webapp.enums.TripState;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Trip {
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;

    @NotBlank
    protected String start;

    @NotBlank
    protected String destination;

    @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm")
    @NotNull
    protected LocalDateTime deparatureDate;

    @DateTimeFormat(pattern="yyyy-MM-dd'T'HH:mm")
    @NotNull
    protected LocalDateTime arrivalDate;

    @Min(1)
    @Max(99)
    protected int numberOfPeople;

    protected TripState tripState;

    @Transient
    protected List<Vehicle> proposedVehicles =  new ArrayList<>();

    @ManyToMany(mappedBy = "tripList", cascade = CascadeType.MERGE)
    protected List<Vehicle> vehicleList =  new ArrayList<>();


    @ManyToOne
    @JoinColumn(name = "id_client", nullable = true)
    protected Client client;

    public Trip(String start, String destination, LocalDateTime deparatureDate, LocalDateTime arrivalDate, int numberOfPeople, Client client) {
        this.tripState = TripState.NEW;
        this.start = start;
        this.destination = destination;
        this.deparatureDate = deparatureDate;
        this.arrivalDate = arrivalDate;
        this.numberOfPeople = numberOfPeople;
        setClient(client);
    }

    public Trip(String start, String destination, LocalDateTime deparatureDate, LocalDateTime arrivalDate, int numberOfPeople) {
        this.tripState = TripState.NEW;
        this.start = start;
        this.destination = destination;
        this.deparatureDate = deparatureDate;
        this.arrivalDate = arrivalDate;
        this.numberOfPeople = numberOfPeople;
    }



    public void addVehicle(Vehicle vehicle){
        this.vehicleList.add(vehicle);
        vehicle.addOrder(this);
    }

    public void setClient(Client client) {
        this.client = client;
        client.addOrder(this);
    }

    public void clearClient() {
        this.client =null;
    }

    public void setProposedVehicles(List<Vehicle> proposedVehicles) {
        this.proposedVehicles = proposedVehicles;
    }

    public List<Vehicle> getProposedVehicles() {
        return proposedVehicles;
    }

    public void addProposedVehicle(Vehicle vehicle){
        proposedVehicles.add(vehicle);
    }

    @Override
    public String toString() {
        return "Trip{" +
                "id=" + id +
                ", start='" + start + '\'' +
                ", cel='" + destination + '\'' +
                ", dataWyjazdu=" + deparatureDate +
                ", dataPowrotu=" + arrivalDate +
                ", iloscOsob=" + numberOfPeople +
                '}';
    }


}

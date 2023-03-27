package com.example.webapp.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
public abstract class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Long id;

    @Pattern(regexp="[\\d]{9}")
    protected String nrTel;

    @NotBlank
    protected String email;

    @OneToMany(mappedBy = "client", cascade = CascadeType.MERGE)
    protected List<Trip> tripList = new ArrayList<>();

    public Client(String nrTel, String email) {
        this.nrTel = nrTel;
        this.email = email;
    }

    public void addOrder(Trip trip) {
        this.tripList.add(trip);
    }
}

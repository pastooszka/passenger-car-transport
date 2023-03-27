package com.example.webapp.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.validation.constraints.NotBlank;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Person extends Client {

    @NotBlank
    protected String name;

    @NotBlank
    protected String surname;

    public Person(String nrTel, String email, String name, String surname) {
        super(nrTel, email);
        this.name = name;
        this.surname = surname;
    }

    @Override
    public String toString() {
        return name + " " + surname;
    }
}

package com.example.webapp.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class Company extends Client {
    @NotBlank
    protected String name;

    @NotBlank
    protected String nip;

    public Company(String nrTel, String email, String name, String nip) {
        super(nrTel, email);
        this.name = name;
        this.nip = nip;
    }

    @Override
    public String toString() {
        return name + " " + nip;
    }
}

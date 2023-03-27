package com.example.webapp;

import com.example.webapp.enums.TripState;
import com.example.webapp.models.*;
import com.example.webapp.repository.ClientRepository;
import com.example.webapp.repository.TripRepository;
import com.example.webapp.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class Start {
    private final VehicleRepository vehicleRepository;
    private final TripRepository tripRepository;
    private final ClientRepository clientRepository;


    @Autowired
    public Start(VehicleRepository vehicleRepository, TripRepository tripRepository, ClientRepository clientRepository) {
        this.vehicleRepository = vehicleRepository;
        this.tripRepository = tripRepository;
        this.clientRepository = clientRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void runExample(){
        Vehicle vehicle1 = new Vehicle("Mercedes","Sprinter","WR 69696", 20);
        Vehicle vehicle2 = new Vehicle("Renualt", "Master", "WR 12345", 16);
        Vehicle vehicle3 = new Vehicle("Opel", "Vivaro", "WR 12345", 8);
        Vehicle vehicle4 = new Vehicle("Renault", "Iliade", "WR 12345", 53);
        Vehicle vehicle5 = new Vehicle("Man", "LionCoach", "WR 12345", 52);
        Vehicle vehicle6 = new Vehicle("Renualt", "Master", "WR 12345", 16);
        Vehicle vehicle7 = new Vehicle("Renualt", "Master", "WR 12345", 16);
        Vehicle vehicle8 = new Vehicle("Vanhool", "t815", "WR 12345", 60);
        Vehicle vehicle9 = new Vehicle("Opel", "Zafira", "WR 12345", 6);
        Vehicle vehicle10 = new Vehicle("Mercedes", "Sprinter", "WR 12345", 20);
        Vehicle vehicle11 = new Vehicle("Mercedes", "Sprinter", "WR 12345", 20);
        Vehicle vehicle12 = new Vehicle("IrisBus", "Iliade", "WR 12345", 53);
        Vehicle vehicle13 = new Vehicle("VW", "LT", "WR 12345", 20);
        Vehicle vehicle14 = new Vehicle("VW", "LT", "WR 12345", 20);



        Trip trip1 =  new Trip("Radom","Warszawa",
                LocalDateTime.of(2023, 01, 5, 6, 30),
                LocalDateTime.of(2023, 01, 5, 18, 20),
                16);
        Trip trip2 =  new Trip("Kozienice","Warszawa",
                LocalDateTime.of(2023, 01, 7, 12, 30),
                LocalDateTime.of(2023, 01, 8, 0, 30),
                8);
        Trip trip3 =  new Trip("Radom","Józefosław",
                LocalDateTime.of(2023, 01, 7, 18, 30),
                LocalDateTime.of(2023, 01, 8, 6, 30),
                20);
        Trip trip4 =  new Trip("Pionki","Warszawa",
                LocalDateTime.of(2023, 01, 8, 6, 40),
                LocalDateTime.of(2023, 01, 8, 11, 30),
                50);
        Trip trip5 =  new Trip("Kozienice","Józefosław",
                LocalDateTime.of(2023, 01, 8, 12, 30),
                LocalDateTime.of(2023, 01, 9, 0, 30),
                99);
        Trip trip6 =  new Trip("Pionki","Józefosław",
                LocalDateTime.of(2023, 01, 7, 18, 30),
                LocalDateTime.of(2023, 01, 8, 6, 30),
                16);
        Trip trip7 =  new Trip("Pionki","Warszawa",
                LocalDateTime.of(2023, 01, 8, 6, 40),
                LocalDateTime.of(2023, 01, 8, 11, 30),
                45);
        Trip trip8 =  new Trip("Pionki","Warszawa",
                LocalDateTime.of(2023, 01, 8, 6, 40),
                LocalDateTime.of(2023, 01, 8, 11, 30),
                35);
        Trip trip9 =  new Trip("Pionki","Warszawa",
                LocalDateTime.of(2023, 01, 8, 6, 40),
                LocalDateTime.of(2023, 01, 8, 11, 30),
                56);
        Trip trip10 =  new Trip("Pionki","Warszawa",
                LocalDateTime.of(2023, 01, 8, 6, 40),
                LocalDateTime.of(2023, 01, 8, 11, 30),
                59);
        Trip trip11 =  new Trip("Pionki","Warszawa",
                LocalDateTime.of(2023, 01, 5, 6, 40),
                LocalDateTime.of(2023, 01, 5, 11, 30),
                99);

        Client klient1 = new Company("123456789","biuro@poczta.pl","Biuro podróży","1233211212");
        Client klient2 = new Person("123456789","jankowalski@poczta.pl","Jan","Kowalski");
        Client klient3 = new Company("123456789","agencjapracy@poczta.pl","Agencja pracy","1233211212");
        Client klient4 = new Person("123456789","kacpernowak@poczta.pl","Kacper","Nowak");


        trip1.addVehicle(vehicle1);
        trip1.setTripState(TripState.PLANNED);

        trip1.setClient(klient1);
        trip2.setClient(klient2);
        trip3.setClient(klient3);
        trip4.setClient(klient4);
        trip5.setClient(klient1);
        trip6.setClient(klient2);
        trip7.setClient(klient3);
        trip8.setClient(klient4);
        trip9.setClient(klient1);
        trip10.setClient(klient2);
        trip11.setClient(klient3);


        clientRepository.save(klient1);
        clientRepository.save(klient2);
        clientRepository.save(klient3);
        clientRepository.save(klient4);

        tripRepository.save(trip1);
        tripRepository.save(trip2);
        tripRepository.save(trip3);
        tripRepository.save(trip4);
        tripRepository.save(trip5);
        tripRepository.save(trip6);
        tripRepository.save(trip7);
        tripRepository.save(trip8);
        tripRepository.save(trip9);
        tripRepository.save(trip10);
        tripRepository.save(trip11);

        vehicleRepository.save(vehicle1);
        vehicleRepository.save(vehicle2);
        vehicleRepository.save(vehicle3);
        vehicleRepository.save(vehicle4);
        vehicleRepository.save(vehicle5);
        vehicleRepository.save(vehicle6);
        vehicleRepository.save(vehicle7);
        vehicleRepository.save(vehicle8);
        vehicleRepository.save(vehicle9);
        vehicleRepository.save(vehicle10);
        vehicleRepository.save(vehicle11);
        vehicleRepository.save(vehicle12);
        vehicleRepository.save(vehicle13);
        vehicleRepository.save(vehicle14);

        }
    }

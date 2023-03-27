package com.example.webapp.services;


import com.example.webapp.models.*;
import com.example.webapp.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class VehicleService {
    private final VehicleRepository vehicleRepository;

    @Autowired
    public VehicleService(VehicleRepository vehicleRepository) {
        this.vehicleRepository = vehicleRepository;
    }

    public List<Vehicle> getAll(){
        List<Vehicle> result = vehicleRepository.findAll();
        result.sort(Comparator.comparing(Vehicle::getBrand));
        return result;

    }

    public void save(Vehicle vehicle){
        vehicleRepository.save(vehicle);
    }

    public Vehicle getById(Long id){
        return vehicleRepository.getReferenceById(id);
    }

    public List<Trip> getTripsList(Long id){
        Vehicle vehicle =  vehicleRepository.getReferenceById(id);
        return vehicle.getTripList();
    }

    public void update(Vehicle vehicle){
            vehicleRepository.getReferenceById(vehicle.getId()).setBrand(vehicle.getBrand());
            vehicleRepository.getReferenceById(vehicle.getId()).setModel(vehicle.getModel());
            vehicleRepository.getReferenceById(vehicle.getId()).setRegNumber(vehicle.getRegNumber());
            vehicleRepository.getReferenceById(vehicle.getId()).setNumberOfSeats(vehicle.getNumberOfSeats());
            vehicleRepository.flush();
    }

    public void delete(Long id){
        vehicleRepository.deleteById(id);
    }
}

package com.example.webapp.services;

import com.example.webapp.enums.TripState;
import com.example.webapp.models.Trip;
import com.example.webapp.models.Vehicle;
import com.example.webapp.repository.TripRepository;
import com.example.webapp.repository.VehicleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class TripService {
    TripRepository tripRepository;
    VehicleRepository vehicleRepository;

    @Autowired
    public TripService(TripRepository tripRepository, VehicleRepository vehicleRepository) {
        this.tripRepository = tripRepository;
        this.vehicleRepository = vehicleRepository;
    }

    public List<Trip> getAll() {
        List<Trip> result = tripRepository.findAll();
        result.sort(Comparator.comparing(Trip::getDeparatureDate).reversed());
        return result;
    }

    public void saveOrder(Trip trip) {
        trip.setTripState(TripState.NEW);
        tripRepository.save(trip);
    }

    public void updateOrder(Trip trip) {
        tripRepository.save(trip);
        tripRepository.flush();
    }

    public Trip getOrderById(Long id) {
        return tripRepository.getReferenceById(id);
    }

    public void deleteOrderById(Long id) {
        Trip trip = tripRepository.getReferenceById(id);
        for (Vehicle vehicle : vehicleRepository.findAll()) {
            if (vehicle.getTripList().contains(trip)) {
                List<Trip> tmp = vehicle.getTripList();
                tmp.remove(trip);
                vehicle.setTripList(tmp);
            }
        }
        tripRepository.deleteById(id);
    }

    public void refresh() {
        for (Trip w : tripRepository.findAll()) {
            if (w.getDeparatureDate().isBefore(LocalDateTime.now())) {
                if (w.getTripState().equals(TripState.PLANNED)) {
                    w.setTripState(TripState.COMPLETED);
                }
                else if (w.getTripState().equals(TripState.NEW)) {
                    w.setTripState(TripState.CANCELED);
                }
            }
        }
        tripRepository.flush();
    }

    public List<Vehicle> getVehicleList(Long id) {
        return tripRepository.getReferenceById(id).getVehicleList();
    }

    public int getNumberOfUnassigned() {
        return getUnassignedOrderList().size();
    }


    //Selekcja pojazdów które są dostępne w danym przedziale czasowym
    public List<Vehicle> getAvailableVehicleList(Trip trip) {
        List<Vehicle> vehicleList = vehicleRepository.findAll();
        for (Trip w : tripRepository.findAll()) {
            if (trip != w) {
                if (!(trip.getArrivalDate().isBefore(w.getDeparatureDate()) ||
                        trip.getDeparatureDate().isAfter(w.getArrivalDate()))) {
                    vehicleList.removeAll(w.getVehicleList());
                    vehicleList.removeAll(w.getProposedVehicles());
                }
            }
        }
        return vehicleList;
    }

    //Lista nieprzypisanych wyjazdów wraz z proponowanymi pojazdami
    public List<Trip> getProposedOrderList() {
        List<Trip> unassignedTripList = getUnassignedOrderList();
        unassignedTripList.sort(Comparator.comparing(Trip::getNumberOfPeople).reversed());


        for (Trip trip : unassignedTripList) {
            System.out.println(trip);
            for (Vehicle p : getGeneratedVehicleList(trip)) {
                trip.addProposedVehicle(p);
            }
        }
        return unassignedTripList;
    }

    //Lista wszystkich wyjazdów które nie mają przypisanych kursów
    public List<Trip> getUnassignedOrderList() {
        List<Trip> unassignedTripList = new ArrayList<>();

        for (Trip w : tripRepository.findAll()) {
            if (w.getTripState().equals(TripState.NEW)) {
                unassignedTripList.add(w);
            }
        }
        return unassignedTripList;
    }


    //Przypisanie proponowanych pojazdów dla wyjazdu według ID
    public void assignOrderByID(long id) {
        for (Trip trip : getProposedOrderList()) {
            if (trip.getId() == id) {
                for (Vehicle vehicle : trip.getProposedVehicles()) {
                    getOrderById(id).addVehicle(vehicle);
                }
                getOrderById(id).setTripState(TripState.PLANNED);
                tripRepository.flush();
            }
        }
    }

    //Przypisanie pojazdów dla wyjazdu według listy podanej jako argument
    public boolean assignManually(long id, List<Vehicle> vehicleList) {
        Trip trip = getOrderById(id);
        int count = 0;
        for (Vehicle vehicle : vehicleList) {
            count += vehicle.getNumberOfSeats();
        }
        if (count >= trip.getNumberOfPeople()) {
            //Usunięcie zależności z obiektu klasy Vehicle
            for (Vehicle vehicle : vehicleRepository.findAll()) {
                if (vehicle.getTripList().contains(trip)) {
                    List<Trip> tmp = vehicle.getTripList();
                    tmp.remove(trip);
                    vehicle.setTripList(tmp);
                }
            }
            for (Vehicle vehicle : vehicleList) {
                trip.addVehicle(vehicle);
            }
            trip.setTripState(TripState.PLANNED);
            tripRepository.flush();
            return false;
        } else {
            return true;
        }
    }

    //Lista pojazdów wygenerowanych przez algorytm dla danego wyjazdu;
    public List<Vehicle> getGeneratedVehicleList(Trip trip) {
        List<Vehicle> generatedVehicleList = new ArrayList<>();

        List<Vehicle> availableVehicleList = getAvailableVehicleList(trip);
        availableVehicleList.sort(Comparator.comparing(Vehicle::getNumberOfSeats));

        int finalNumberOfSeats = 0;
        int numberOfPeople = trip.getNumberOfPeople();

        while (finalNumberOfSeats < trip.getNumberOfPeople() && availableVehicleList.size() != 0) {
            for (Vehicle vehicle : availableVehicleList) {
                if (numberOfPeople <= vehicle.getNumberOfSeats()) {
                    generatedVehicleList.add(vehicle);
                    availableVehicleList.remove(vehicle);
                    finalNumberOfSeats += vehicle.getNumberOfSeats();

                    break;

                } else if (vehicle.equals(availableVehicleList.get(availableVehicleList.size() - 1))) {
                    generatedVehicleList.add(vehicle);
                    availableVehicleList.remove(vehicle);

                    finalNumberOfSeats += vehicle.getNumberOfSeats();
                    numberOfPeople -= vehicle.getNumberOfSeats();

                    break;
                }
            }
        }
        if (finalNumberOfSeats < numberOfPeople) {
            generatedVehicleList.clear();
        }
        return generatedVehicleList;
    }


}

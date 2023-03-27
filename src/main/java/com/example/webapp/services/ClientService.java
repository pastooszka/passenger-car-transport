package com.example.webapp.services;

import com.example.webapp.models.*;
import com.example.webapp.repository.ClientRepository;
import com.example.webapp.repository.TripRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


    @Service
    public class ClientService {
        private final ClientRepository clientRepository;
        private final TripRepository tripRepository;

        @Autowired
        public ClientService(ClientRepository clientRepository, TripRepository tripRepository) {
            this.clientRepository = clientRepository;
            this.tripRepository = tripRepository;
        }

        public List<Client> getAll(){
            return clientRepository.findAll();
        }

        public List<Client> getCompanies(){
            List<Client> companies = new ArrayList<>();
            for (Client k : clientRepository.findAll()) {
                if (k.getClass()== Company.class){
                    companies.add(k);
                }
            }
            return companies;
        }

        public List<Client> getPersons(){
            List<Client> persons = new ArrayList<>();
            for (Client k : clientRepository.findAll()) {
                if (k.getClass() == Person.class){
                    persons.add(k);
                }
            }
            return persons;
        }

    public void save(Client client){
        clientRepository.save(client);
    }

    public Client getById(Long id){
        return clientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user ID:" + id));
    }

    public Client getByOrder(Trip trip){
        for (Client client : clientRepository.findAll()){
            if (client.getTripList().contains(trip)){
                return client;
            }
        }
        return null;
    }

    public List<Trip> getOrders(Long id){
        Client client =  clientRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid user ID:" + id));
        return client.getTripList();
    }

    public void updateCompany(Client client) {
        clientRepository.saveAndFlush(client);
    }

    public void updatePerson(Client client) {
        clientRepository.saveAndFlush(client);
    }

    public void deleteByID(Long id){
        for (Trip trip : clientRepository.getReferenceById(id).getTripList()){
            trip.clearClient();
        }
        tripRepository.flush();
        clientRepository.deleteById(id);
    }
}

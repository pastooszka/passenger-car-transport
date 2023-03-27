package com.example.webapp.controller;

import com.example.webapp.enums.TripState;
import com.example.webapp.models.Trip;
import com.example.webapp.services.ClientService;
import com.example.webapp.services.VehicleService;
import com.example.webapp.services.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class TripController {
    private final TripService tripService;
    private final ClientService clientService;
    private final VehicleService vehicleService;

    @Autowired
    public TripController(TripService tripService, ClientService clientService, VehicleService vehicleService) {
        this.tripService = tripService;
        this.clientService = clientService;
        this.vehicleService = vehicleService;
    }

    @GetMapping("/trips")
    public String showTripList(Model model) {
        tripService.refresh();
        model.addAttribute("trips", tripService.getAll());
        model.addAttribute("types", TripState.values());
        return "trips";
    }


    @GetMapping("/trips/add")
    public String showTripForm(Trip trip, Model model, HttpSession session) {
        model.addAttribute("trip", trip);
        model.addAttribute("clients", clientService.getAll());
        model.addAttribute("vehicles", vehicleService.getAll());
        return "trips/add";
    }

    @PostMapping("/addTrip")
    public String addTrip(@ModelAttribute("trip") @Valid Trip trip, BindingResult result, Model model) {
        model.addAttribute("clients", clientService.getAll());
        model.addAttribute("vehicles", vehicleService.getAll());
        if (result.hasErrors()) {
            return "trips/add";
        } else {
            tripService.saveOrder(trip);
            return "redirect:/trips";
        }
    }

    @GetMapping("/trips/details/{id}")
    public String showVehicleDetails(@PathVariable("id") Long id, Model model, HttpSession session) {
        Trip trip = tripService.getOrderById(id);
        trip.setProposedVehicles(tripService.getGeneratedVehicleList(trip));
        model.addAttribute("trip", trip);
        model.addAttribute("availableVehicles", tripService.getAvailableVehicleList(tripService.getOrderById(id)));
        model.addAttribute("vehicles", tripService.getVehicleList(id));
        return "trips/details";
    }

    @PostMapping("/assignManually/{id}")
    public String assignTripManually(@ModelAttribute("trip") Trip trip, @PathVariable("id") Long id, Model model) {
        boolean result = tripService.assignManually(id, trip.getProposedVehicles());
        if (result) {
            Trip w = tripService.getOrderById(id);
            w.setProposedVehicles(tripService.getGeneratedVehicleList(w));
            model.addAttribute("trip", w);
            model.addAttribute("vehicles", tripService.getVehicleList(id));
            model.addAttribute("availableVehicles", tripService.getAvailableVehicleList(tripService.getOrderById(id)));
            model.addAttribute("error", true);
            return "trips/details";
        } else {
            return "redirect:/trips/details/{id}";
        }
    }

    @GetMapping("/trips/editform/{id}")
    public String showTripEditForm(@PathVariable("id") Long id, Model model, HttpSession session) {
        model.addAttribute("trip", tripService.getOrderById(id));
        model.addAttribute("clients", clientService.getAll());
        model.addAttribute("vehicles", vehicleService.getAll());
        return "trips/edit";
    }


    @PostMapping("/updateTrip/{id}")
    public String updateKierowca(@ModelAttribute("trip") @Valid Trip trip, BindingResult result, @PathVariable("id") Long id, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("clients", clientService.getAll());
            model.addAttribute("vehicles", vehicleService.getAll());
            System.out.println(trip);
            return "trips/edit";
        } else {
            trip.setTripState(tripService.getOrderById(id).getTripState());
            tripService.updateOrder(trip);
            return "redirect:/trips";
        }
    }

    @GetMapping("/trips/assign")
    public String assignTrips(Model model, HttpSession session) {
        model.addAttribute("trips", tripService.getProposedOrderList());
        return "trips/unassigned";
    }

    @GetMapping("/trips/assign/{id}")
    public String assignTrip(@PathVariable("id") Long id, Model model, HttpSession session) {
        tripService.assignOrderByID(id);
        return "redirect:/trips/assign";
    }

    @RequestMapping(value = "/trips/delete/{id}", method = {RequestMethod.DELETE, RequestMethod.GET})
    public String deleteTrip(@PathVariable("id") Long id) {
        tripService.deleteOrderById(id);
        return "redirect:/trips";
    }

    @RequestMapping(value = "/trips/assign/delete/{id}", method = {RequestMethod.DELETE, RequestMethod.GET})
    public String deleteAssignTrip(@PathVariable("id") Long id) {
        tripService.deleteOrderById(id);
        return "redirect:/trips/assign";
    }


}


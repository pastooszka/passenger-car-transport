package com.example.webapp.controller;

import com.example.webapp.models.Vehicle;
import com.example.webapp.services.VehicleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

@Controller
public class VehicleController {

    private final VehicleService vehicleService;

    @Autowired
    public VehicleController(VehicleService vehicleService) {
        this.vehicleService = vehicleService;
    }

    @GetMapping("/vehicles")
    public String showVehicleList(Model model){
        model.addAttribute("vehicles", vehicleService.getAll());
        return "vehicles";
    }

    @GetMapping("/vehicles/add")
    public String showVehicleForm(Vehicle vehicle, Model model){
        model.addAttribute("vehicle", vehicle);
        return "vehicles/add";
    }

    @PostMapping("/addVehicle")
    public String addVehicle(@ModelAttribute("vehicle") @Valid Vehicle vehicle, BindingResult result){
        if (result.hasErrors()){
            return "vehicles/add";
        }else {
            vehicleService.save(vehicle);
            return "redirect:/vehicles";
        }
    }


    @RequestMapping(value="/vehicles/delete/{id}", method={RequestMethod.DELETE, RequestMethod.GET})
    public String deleteVehicle(@PathVariable("id") Long id){
        vehicleService.delete(id);
        return "redirect:/vehicles";
    }

    @GetMapping("/vehicles/details/{id}")
    public String showVehicleDetails(@PathVariable("id") Long id, Model model, HttpSession session){
        model.addAttribute("vehicle", vehicleService.getById(id));
        model.addAttribute("trips", vehicleService.getTripsList(id));
        return "vehicles/details";
    }

    @GetMapping("/vehicles/editform/{id}")
    public String showVehicleEditForm(@PathVariable("id") Long id, Model model, HttpSession session){
        model.addAttribute("vehicle", vehicleService.getById(id));
        return "vehicles/edit";
    }

    @PostMapping("/updateVehicle/{id}")
    public String updateVehicle(@ModelAttribute("vehicle") @Valid Vehicle vehicle, BindingResult result, @PathVariable("id") Long id, Model model){
        if (result.hasErrors()){
            return "vehicles/edit";
        }else {
            vehicleService.update(vehicle);
            return "redirect:/vehicles";
        }
    }

}

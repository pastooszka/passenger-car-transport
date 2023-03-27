package com.example.webapp.controller;

import com.example.webapp.services.TripService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    TripService tripService;

    @Autowired
    public IndexController(TripService tripService) {
        this.tripService = tripService;
    }

    @GetMapping("/index")
    public String showIndex(Model model){
        tripService.refresh();
        model.addAttribute("wyjazdy", tripService.getNumberOfUnassigned());
        return "index";
    }
}

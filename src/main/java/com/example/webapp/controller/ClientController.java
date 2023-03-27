package com.example.webapp.controller;

import com.example.webapp.models.Client;
import com.example.webapp.models.Company;
import com.example.webapp.models.Person;
import com.example.webapp.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Controller
public class ClientController {
    private final ClientService clientService;
    List<String> types = new ArrayList<>();


    @Autowired
    public ClientController(ClientService clientService){
        this.clientService = clientService;
        types.add("Osoba prywatna");
        types.add("Company");
    }


    @GetMapping("/clients")
    public String showKlientList(Model model){
        model.addAttribute("persons", clientService.getPersons());
        model.addAttribute("companies", clientService.getCompanies());
        return "clients";
    }

    @GetMapping("/clients/add")
    public String showKlientForm(Person person, Company company, Model model, HttpSession session){
        model.addAttribute("person", person);
        model.addAttribute("company", company);
        model.addAttribute("types", types);
        return "clients/add";
    }

    @GetMapping("/clients/companydetails/{id}")
    public String showOsobaDetails(@PathVariable("id") Long id, Model model, HttpSession session){
        Client client = clientService.getById(id);
        model.addAttribute("company", client);
        model.addAttribute("trips", clientService.getOrders(id));
        return "clients/companydetails";
    }

    @GetMapping("/clients/persondetails/{id}")
    public String showFirmaDetails(@PathVariable("id") Long id, Model model, HttpSession session){
        Client client = clientService.getById(id);
        model.addAttribute("person", client);
        model.addAttribute("trips", clientService.getOrders(id));
        return "clients/persondetails";
    }

    @PostMapping("/addPerson")
    public String addOsoba(@ModelAttribute("person") @Valid Person person, BindingResult result, @ModelAttribute("company") Company company, Model model){
        if (result.hasErrors()){
            Collections.sort(types);
            Collections.reverse(types);
            model.addAttribute("types", types);
            return "clients/add";
        }else {
            clientService.save(person);
            return "redirect:/clients";
        }
    }

    @PostMapping("/addCompany")
    public String addFirma(@ModelAttribute("company") @Valid Company company, BindingResult result, @ModelAttribute("person") Person person, Model model){
        if (result.hasErrors()){
            Collections.sort(types);
            model.addAttribute("types", types);
            return "clients/add";
        }else {
            clientService.save(company);
            return "redirect:/clients";
        }
    }

    @GetMapping("/clients/editform/{id}")
    public String showPojazdEditForm(@PathVariable("id") Long id, Model model, HttpSession session){
        if (clientService.getById(id).getClass().equals(Company.class)){
            model.addAttribute("showCompany",true);
            model.addAttribute("company", (Company) clientService.getById(id));
        }else {
            model.addAttribute("showPerson",true);
            model.addAttribute("person", (Person) clientService.getById(id));
        }

        return "clients/edit";
    }

    @PostMapping("/updateCompany/{id}")
    public String updateFirma(@ModelAttribute("company") @Valid Company company, BindingResult result, @PathVariable("id") Long id, Model model){
        if (result.hasErrors()){
            model.addAttribute("showCompany",true);
            return "clients/edit";
        }else {
            clientService.updateCompany(company);
            return "redirect:/clients";
        }
    }

    @PostMapping("/updatePerson/{id}")
    public String updateFirma(@ModelAttribute("person") @Valid Person person, BindingResult result, @PathVariable("id") Long id, Model model){
        if (result.hasErrors()){
            model.addAttribute("showPerson",true);
            return "clients/edit";
        }else {
            clientService.updatePerson(person);
            return "redirect:/clients";
        }
    }

    @RequestMapping(value= "/clients/delete/{id}", method={RequestMethod.DELETE, RequestMethod.GET})
    public String deletePojazd(@PathVariable("id") Long id){
        clientService.deleteByID(id);
        return "redirect:/clients";
    }


}

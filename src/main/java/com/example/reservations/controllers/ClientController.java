package com.example.reservations.controllers;


import com.example.reservations.domain.Client;
import com.example.reservations.dto.ClientDto;
import com.example.reservations.repositories.ClientRepo;
import com.example.reservations.services.implementation.ClientSerImp;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/client")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ClientController {

    @Autowired
    private ClientSerImp clientSerImp;
    @Autowired
    private ClientRepo clientRepo;


    @PostMapping("/save")
    public ClientDto saveClient(@Valid @RequestBody ClientDto clientDto) {
        return clientSerImp.addClient(clientDto);
    }

    @GetMapping("/all")
    public List<ClientDto> getAllClients() {
        return clientSerImp.getAllClients();
    }

    @GetMapping("/byEmail")
    public Client getClientByEmail(@RequestBody String email)
    {
        Client client =clientRepo.findByEmail(email);
        return client;
    }


}

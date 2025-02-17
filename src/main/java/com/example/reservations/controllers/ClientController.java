package com.example.reservations.controllers;

import com.example.reservations.dto.ClientDto;
import com.example.reservations.dto.ResponseClientDto;
import com.example.reservations.services.implementation.ClientSerImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientSerImp clientSerImp;

    @PostMapping("/save")
    public ClientDto saveClient(@RequestBody ClientDto clientDto) {
        return clientSerImp.addClient(clientDto);
    }

    @GetMapping("/all")
    public List<ClientDto> getAllClients() {
        return clientSerImp.getAllClients();
    }

    @GetMapping("/byEmail")
    public  ResponseClientDto getClientByEmail(@RequestParam String email) {
        return clientSerImp.findByEmail(email);
    }

    @GetMapping("/apres-date")
    public List<ClientDto> getClientsInscritsApresDate(@RequestParam LocalDateTime date) {
        return clientSerImp.getClientsInscritsApresDate(date);
    }

    @GetMapping("/avec-reservations")
    public List<ClientDto> getClientsAvecReservations() {
        return clientSerImp.getClientsAvecReservations();
    }
}
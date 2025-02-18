package com.example.reservations.controllers;

import com.example.reservations.dto.ClientDto;
import com.example.reservations.dto.Response;
import com.example.reservations.services.implementation.ClientSerImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientSerImp clientSerImp;

    // Save client
    @PostMapping("/save")
    public ResponseEntity<Object> saveClient(@RequestBody ClientDto clientDto) {
        ClientDto savedClient = clientSerImp.addClient(clientDto);
        String message = "Client saved successfully.";
        return new ResponseEntity<>(new Response(savedClient, message), HttpStatus.CREATED);
    }

    // Get all clients
    @GetMapping("/all")
    public ResponseEntity<Object> getAllClients() {
        List<ClientDto> clients = clientSerImp.getAllClients();
        if (clients.isEmpty()) {
            String message = "No clients found.";
            return new ResponseEntity<>(new Response(null, message), HttpStatus.NO_CONTENT); // 204 if no clients
        }
        String message = "Clients retrieved successfully.";
        return new ResponseEntity<>(new Response(clients, message), HttpStatus.OK);
    }

    // Get client by email
    @GetMapping("/byEmail")
    public ResponseEntity<Object> getClientByEmail(@RequestParam String email) {
        Optional<ClientDto> client = clientSerImp.findByEmail(email);
        if (client.isEmpty()) {
            String message = "Client not found with the given email.";
            return new ResponseEntity<>(new Response(null, message), HttpStatus.NOT_FOUND); // 404 if client not found
        }
        String message = "Client retrieved successfully.";
        return new ResponseEntity<>(new Response(client.get(), message), HttpStatus.OK);
    }

    // Get clients who registered after a specific date
    @GetMapping("/apres-date")
    public ResponseEntity<Object> getClientsInscritsApresDate(@RequestParam LocalDateTime date) {
        List<ClientDto> clients = clientSerImp.getClientsInscritsApresDate(date);
        if (clients.isEmpty()) {
            String message = "No clients found who registered after the given date.";
            return new ResponseEntity<>(new Response(null, message), HttpStatus.NO_CONTENT); // 204 if no clients found
        }
        String message = "Clients retrieved successfully.";
        return new ResponseEntity<>(new Response(clients, message), HttpStatus.OK);
    }

    // Get clients who have made reservations
    @GetMapping("/avec-reservations")
    public ResponseEntity<Object> getClientsAvecReservations() {
        List<ClientDto> clients = clientSerImp.getClientsAvecReservations();
        if (clients.isEmpty()) {
            String message = "No clients found with reservations.";
            return new ResponseEntity<>(new Response(null, message), HttpStatus.NO_CONTENT); // 204 if no clients
        }
        String message = "Clients with reservations retrieved successfully.";
        return new ResponseEntity<>(new Response(clients, message), HttpStatus.OK);
    }
}

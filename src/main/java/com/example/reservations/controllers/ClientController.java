package com.example.reservations.controllers;

import com.example.reservations.domain.Client;
import com.example.reservations.dto.ClientDto;
import com.example.reservations.services.implementation.ClientSerImp;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/client")
public class ClientController {

    @Autowired
    private ClientSerImp clientSerImp;

    /**
     * API to save a new client.
     * This endpoint accepts a ClientDto in the request body and saves the client in the system.
     * If the ID is provided, it returns a 400 Bad Request response as the ID will be generated automatically.
     * If the email is missing, it returns a 400 Bad Request response.
     * If the client is successfully created, it returns a 201 Created response with the saved client data.
     * In case of failure, it returns a 500 Internal Server Error response.
     */
    @PostMapping("/save")
    public ResponseEntity<ClientDto> saveClient(@Valid @RequestBody ClientDto clientDto) {
        if (clientDto.getId() != null) {
            clientDto.setMessageFr("Vous ne pouvez pas fournir un ID, il sera généré automatiquement.");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(clientDto); // 400 if ID is provided
        }

        if (clientDto.getEmail() == null || clientDto.getEmail().isEmpty()) {
            clientDto.setMessageFr("L'email est requis.");

            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(clientDto); // 400 if email is missing
        }

        try {
            ClientDto savedClient = clientSerImp.addClient(clientDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedClient); // 201 if successful
        } catch (Exception e) {
            clientDto.setMessageFr("Erreur lors de la création du client.");

            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(clientDto); // 500 if failure
        }
    }

    /**
     * API to get all clients.
     * This endpoint fetches all clients from the system.
     * If no clients are found, it returns a 204 No Content response.
     * If clients are found, it returns a 200 OK response with the list of clients.
     */
    @GetMapping("/all")
    public ResponseEntity<List<ClientDto>> getAllClients() {
        List<ClientDto> clients = clientSerImp.getAllClients();

        if (clients.isEmpty()) {
            ClientDto clientDto = new ClientDto();
            clientDto.setMessageFr("Aucun client trouvé.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(clients); // 204 if no clients found
        }

        return ResponseEntity.status(HttpStatus.OK).body(clients); // 200 if clients found
    }

    /**
     * API to get a client by email.
     * This endpoint retrieves a client based on the provided email.
     * If no client is found, it returns a 404 Not Found response.
     * If the client is found, it returns a 200 OK response with the client data.
     */
    @GetMapping("/byEmail")
    public ResponseEntity<ClientDto> getClientByEmail(@RequestBody String email) {
        Optional<ClientDto> client = clientSerImp.findByEmail(email);
        if (client.isEmpty()) {
            ClientDto clientDto = new ClientDto();
            clientDto.setMessageFr("Client non trouvé avec l'email fourni.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(clientDto); // 404 if client not found
        }
        ClientDto foundClient = client.get();
        foundClient.setMessageFr("Client trouvé avec succès.");
        return ResponseEntity.status(HttpStatus.OK).body(foundClient); // 200 if client found
    }

    /**
     * API to get clients who registered after a specific date.
     * This endpoint fetches all clients who registered after the provided date.
     * If no clients are found, it returns a 204 No Content response.
     * If clients are found, it returns a 200 OK response with the list of clients.
     */
    @GetMapping("/apres-date")
    public ResponseEntity<List<Client>> getClientsInscritsApresDate(@RequestParam LocalDateTime date) {
        List<Client> clients = clientSerImp.getClientsInscritsApresDate(date);
        try {
            if (clients.isEmpty()) {
                Client client = new Client();
                client.setMessageFr("Aucun client inscrit après cette date.");
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body(clients); // 204 if no clients found
            }

            return ResponseEntity.status(HttpStatus.OK).body(clients); // 200 if clients found
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).body(clients);
    }

    /**
     * API to get clients who have made reservations.
     * This endpoint retrieves all clients who have made at least one reservation.
     * If no clients are found, it returns a 204 No Content response.
     * If clients are found, it returns a 200 OK response with the list of clients.
     */
    @GetMapping("/avec-reservations")
    public ResponseEntity<List<Client>> getClientsAvecReservations() {
        List<Client> clients = clientSerImp.getClientsAvecReservations();
        if (clients.isEmpty()) {
            ClientDto clientDto = new ClientDto();
            clientDto.setMessageFr("Aucun client trouvé avec des réservations.");
            return ResponseEntity.status(HttpStatus.NO_CONTENT).body(clients); // 204 if no clients found
        }
        return ResponseEntity.status(HttpStatus.OK).body(clients); // 200 if clients found
    }
}

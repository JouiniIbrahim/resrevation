package com.example.reservations.services.implementation;

import com.example.reservations.domain.Client;
import com.example.reservations.dto.ClientDto;
import com.example.reservations.mapper.ClientMapper;
import com.example.reservations.repositories.ClientRepo;
import com.example.reservations.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ClientSerImp implements ClientService {

    @Autowired
    private ClientRepo clientRepo;

    @Override
    public ClientDto addClient(ClientDto clientDto) {
        Client client = ClientMapper.ToEntity(clientDto);
        clientRepo.save(client);
        return ClientMapper.ToDTO(client);
    }

    @Override
    public ClientDto updateClient(ClientDto clientDto) {
        Client existingClient = clientRepo.findById(clientDto.getId()).orElseThrow(() -> new RuntimeException("Client non trouvé"));
        existingClient = ClientMapper.ToEntity(clientDto);
        clientRepo.save(existingClient);
        return ClientMapper.ToDTO(existingClient);
    }

    /**
     * Retrieve all clients as ClientDto objects.
     *
     * @return List of ClientDto representing all clients.
     */
    @Override
    public List<ClientDto> getAllClients() {
        return clientRepo.findAllClients();  // Directly use the custom query in the repository
    }

    /**
     * Retrieve all clients who registered after a given date.
     *
     * @param date The date to filter client registrations.
     * @return List of ClientDto representing the clients who registered after the given date.
     */
    @Override
    public List<Client> getClientsInscritsApresDate(LocalDateTime date) {

        List<Client> clients = clientRepo.findByDateInscriptionAfter(date);

        return clients;
    }

    /**
     * Retrieve all clients who have made at least one reservation.
     *
     * @return List of ClientDto representing the clients who have made at least one reservation.
     */
    @Override
    public List<Client> getClientsAvecReservations() {
        // Fetch the clients from the repository
        List<Client> clients = clientRepo.findClientsWithReservations();
        return clients;
    }

    /**
     * Find a client by email.
     *
     * @param email The email of the client to find.
     * @return An Optional containing ClientDto if found, or empty if no client is found.
     */
    @Override
    public Optional<ClientDto> findByEmail(String email) {
        return clientRepo.findByEmail(email);
    }
}
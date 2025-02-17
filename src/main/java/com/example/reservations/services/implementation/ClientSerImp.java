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
import java.util.stream.Collectors;

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
    // Nouvelle méthode pour trouver un client par email
    @Override
    public ClientDto findByEmail(String email) {
        Client client = clientRepo.findByEmail(email);
        if (client == null) {
            throw new RuntimeException("Client non trouvé avec l'email : " + email);
        }
        return ClientMapper.ToDTO(client);
    }

    @Override
    public List<ClientDto> getAllClients() {
        return clientRepo.findAll().stream().map(ClientMapper::ToDTO).collect(Collectors.toList());
    }

    // Nouvelle méthode pour trouver les clients inscrits après une date donnée
    public List<ClientDto> getClientsInscritsApresDate(LocalDateTime date) {
        return clientRepo.findByDateInscriptionAfter(date).stream().map(ClientMapper::ToDTO).collect(Collectors.toList());
    }

    // Nouvelle méthode pour trouver les clients ayant effectué au moins une réservation
    public List<ClientDto> getClientsAvecReservations() {
        return clientRepo.findClientsWithReservations().stream().map(ClientMapper::ToDTO).collect(Collectors.toList());
    }
}
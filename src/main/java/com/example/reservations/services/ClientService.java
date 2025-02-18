package com.example.reservations.services;

import com.example.reservations.dto.ClientDto;
import com.example.reservations.dto.ResponseClientDto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ClientService {
    ClientDto addClient(ClientDto clientDto);

    ClientDto updateClient(ClientDto clientDto);

    List<ClientDto> getAllClients();

    //ClientDto findByEmail(String email);
    Optional<ClientDto> findByEmail(String email);

    public List<ClientDto> getClientsInscritsApresDate(LocalDateTime date);

    public List<ClientDto> getClientsAvecReservations();


}

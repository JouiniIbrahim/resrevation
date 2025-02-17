package com.example.reservations.services;

import com.example.reservations.dto.ClientDto;

import java.util.List;

public interface ClientService {
    ClientDto addClient(ClientDto clientDto);
    ClientDto updateClient(ClientDto clientDto);
    List<ClientDto> getAllClients();
    ClientDto findByEmail(String email);




}

package com.example.reservations.services;

import com.example.reservations.dto.ClientDto;

import java.util.List;

public interface ClientService {

    public ClientDto addClient(ClientDto clientDto);

    public ClientDto updateClient(ClientDto clientDto);

    public List<ClientDto> getAllClients();




}

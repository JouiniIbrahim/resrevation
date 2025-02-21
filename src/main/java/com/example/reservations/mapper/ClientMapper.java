package com.example.reservations.mapper;

import com.example.reservations.domain.Client;
import com.example.reservations.dto.ClientDto;

import java.time.LocalDateTime;

public class ClientMapper {

    public static ClientDto ToDTO(Client client) {
        if (client == null) {
            return null;
        }
        ClientDto clientDto = new ClientDto();
        clientDto.setId(client.getId());
        clientDto.setNom(client.getNom());
        clientDto.setPrenom(client.getPrenom());
        clientDto.setEmail(client.getEmail());
        clientDto.setTelephone(client.getTelephone());
        clientDto.setDateInscription(LocalDateTime.now());
        return clientDto;
    }

    public static Client ToEntity(ClientDto clientDto) {
        if (clientDto == null) {
            return null;
        }
        Client client = new Client();
        client.setId(clientDto.getId());
        client.setNom(clientDto.getNom());
        client.setPrenom(clientDto.getPrenom());
        client.setEmail(clientDto.getEmail());
        client.setTelephone(clientDto.getTelephone());
        client.setDateInscription(LocalDateTime.now());
        return client;
    }
}

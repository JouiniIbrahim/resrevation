package com.example.reservations.services.implementation;

import com.example.reservations.domain.Client;
import com.example.reservations.dto.ClientDto;
import com.example.reservations.mapper.ClientMapper;
import com.example.reservations.repositories.ClientRepo;
import com.example.reservations.services.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.reservations.mapper.ClientMapper.ToDTO;
import static com.example.reservations.mapper.ClientMapper.ToEntity;

@Service
public class ClientSerImp implements ClientService {

    @Autowired
    ClientRepo clientRepo;

    @Override
    public ClientDto addClient(ClientDto clientDto) {
        Client client = ToEntity(clientDto);
        clientRepo.save(client);
        return ToDTO(client);
    }

    @Override
    public ClientDto updateClient(ClientDto clientDto) {
        Client existingClient = clientRepo.findById(clientDto.getId()).get();

        existingClient = ToEntity(clientDto);
        clientRepo.save(existingClient);
        return ToDTO(existingClient);

    }

    @Override
    public List<ClientDto> getAllClients()
    {
        return clientRepo.findAll().stream().map(ClientMapper::ToDTO)
                .collect(Collectors.toList());

    }

}

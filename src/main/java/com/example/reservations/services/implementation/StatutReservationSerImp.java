package com.example.reservations.services.implementation;


import com.example.reservations.domain.StatutReservation;
import com.example.reservations.dto.StatutReservationDto;

import com.example.reservations.mapper.StatutReservationMapper;
import com.example.reservations.repositories.StatutReservationRepo;
import com.example.reservations.services.StatutReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.example.reservations.mapper.StatutReservationMapper.ToDTO;
import static com.example.reservations.mapper.StatutReservationMapper.ToEntity;

@Service
public class StatutReservationSerImp implements StatutReservationService {

    @Autowired
    StatutReservationRepo statutReservationRepo;

    @Override
    public StatutReservationDto saveStatutReservation(StatutReservationDto statutReservationDto) {
        boolean statutExists = getAllStatutReservations().stream()
                .anyMatch(statut -> (statut.getLibelle()).equals(statutReservationDto.getLibelle()));

        if (!statutExists) {
            StatutReservation statutReservation = ToEntity(statutReservationDto);
            statutReservationRepo.save(statutReservation);
            return ToDTO(statutReservation);
        }
        return null;
    }

    public List<StatutReservationDto> getAllStatutReservations() {
        return statutReservationRepo.findAll().stream().map(StatutReservationMapper::ToDTO)
                .collect(Collectors.toList());
    }
}

package com.example.reservations.services;

import com.example.reservations.domain.StatutReservation;
import com.example.reservations.dto.StatutReservationDto;

import java.util.List;

public interface StatutReservationService {

    public List<StatutReservationDto> getAllStatutReservations();


    public StatutReservationDto saveStatutReservation(StatutReservationDto statutReservation);

}

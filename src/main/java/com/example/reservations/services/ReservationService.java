package com.example.reservations.services;

import com.example.reservations.dto.ReservationDto;

import java.util.List;

public interface ReservationService {

    public ReservationDto create(ReservationDto reservationDto);

    public List<ReservationDto> findAll();

}

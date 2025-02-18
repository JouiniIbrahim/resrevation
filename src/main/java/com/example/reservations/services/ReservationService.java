package com.example.reservations.services;

import com.example.reservations.domain.Client;
import com.example.reservations.dto.ClientDto;
import com.example.reservations.dto.ReservationDto;
import com.example.reservations.dto.ReservationResponseDto;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationService {

    public ReservationDto create(ReservationDto reservationDto);

    public List<ReservationResponseDto> findAll();

    public List<ReservationResponseDto>
    findReservations(Long statutId, LocalDate startDate, LocalDate endDate);

    public Double TotalRevenue(Long statutId, LocalDate startDate, LocalDate endDate);

    public List<Client> ReservationsMore(int year, int x);

}

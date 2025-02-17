package com.example.reservations.services;

import com.example.reservations.dto.ClientDto;
import com.example.reservations.dto.ReservationDto;

import java.time.LocalDate;
import java.util.List;

public interface ReservationService {

    public ReservationDto create(ReservationDto reservationDto);

    public List<ReservationDto> findAll();

    public List<ReservationDto> findReservations(String statut, LocalDate startDate, LocalDate endDate);

    public Double TotalRevenue(String statut, LocalDate startDate, LocalDate endDate);

    public List<ClientDto> ReservationsMore(int year, int x);

}

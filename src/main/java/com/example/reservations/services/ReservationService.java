package com.example.reservations.services;

import com.example.reservations.domain.Client;
import com.example.reservations.dto.ClientDto;
import com.example.reservations.dto.ReservationDto;
import com.example.reservations.dto.ReservationResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ReservationService {

    public ReservationDto create(ReservationDto reservationDto);

    public Page<ReservationResponseDto> findAll(Pageable pageable);

    public List<ReservationResponseDto>
    findReservations(Long statutId, LocalDate startDate, LocalDate endDate);

    public Double TotalRevenue(Long statutId, LocalDate startDate, LocalDate endDate);

    public List<Client> ReservationsMore(int year, int x);

}

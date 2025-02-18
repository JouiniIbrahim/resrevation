package com.example.reservations.services.implementation;

import com.example.reservations.domain.Client;
import com.example.reservations.domain.Reservation;
import com.example.reservations.dto.ClientDto;
import com.example.reservations.dto.ReservationDto;
import com.example.reservations.dto.ReservationResponseDto;
import com.example.reservations.mapper.ClientMapper;
import com.example.reservations.mapper.ReservationMapper;
import com.example.reservations.repositories.ReservationRepo;
import com.example.reservations.services.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReservationSerImp implements ReservationService {

    @Autowired
    private ReservationRepo reservationRepo;

    @Autowired
    private ReservationMapper reservationMapper;

    @Override
    public ReservationDto create(ReservationDto reservationDto) {
        Reservation reservation = reservationMapper.ToEntity(reservationDto);
        reservationRepo.save(reservation);
        return reservationMapper.ToDTO(reservation);
    }

    @Override
    public List<ReservationResponseDto> findAll() {
        return reservationRepo.allReservations();
    }

    //  récupérer les réservations par statut et période
    @Override
    public List<ReservationResponseDto> findReservations(Long statutId, LocalDate startDate, LocalDate endDate) {
        return reservationRepo.findReservationsByStatutAndPeriod(statutId, startDate, endDate);

    }

    //  calculer le revenu total par statut et période
    @Override
    public Double TotalRevenue(Long statutId, LocalDate startDate, LocalDate endDate) {
        return reservationRepo.TotalRevenue(statutId, startDate, endDate);
    }

    //  trouver les clients ayant réservé plus de X fois dans une année
    @Override
    public List<Client> ReservationsMore(int year, int x) {
        return reservationRepo.ReservationsMore(year, x);

    }
}

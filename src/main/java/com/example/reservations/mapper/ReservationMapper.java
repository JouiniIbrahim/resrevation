package com.example.reservations.mapper;

import com.example.reservations.domain.Reservation;

import com.example.reservations.dto.ReservationDto;
import com.example.reservations.repositories.ClientRepo;
import com.example.reservations.repositories.StatutReservationRepo;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class ReservationMapper {
    @Autowired
   private  StatutReservationRepo statutReservationRepo;
    @Autowired
    private  ClientRepo clientRepo;

    public  ReservationDto ToDTO(Reservation reservation) {
        if (reservation == null) {
            return null;
        }
        ReservationDto reservationDto = new ReservationDto();
        reservationDto.setId(reservation.getId());
        reservationDto.setDateDebut(reservation.getDateDebut());
        reservationDto.setDateFin(reservation.getDateFin());
        reservationDto.setPrixTotal(reservation.getPrixTotal());
        reservationDto.setClient(ClientMapper.ToDTO(reservation.getClient()));
        reservationDto.setStatut(StatutReservationMapper.ToDTO(reservation.getStatut()));

        return reservationDto;
    }

    public  Reservation ToEntity(ReservationDto reservationDto) {
        if (reservationDto == null)
        {
            return null;
        }
        Reservation reservation = new Reservation();
       // reservation.setId(reservationDto.getId());
        reservation.setDateDebut(reservationDto.getDateDebut());
        reservation.setDateFin(reservationDto.getDateFin());
        reservation.setPrixTotal(reservationDto.getPrixTotal());

        reservation.setStatut(statutReservationRepo.findById(reservationDto.getStatutId()).get());
        reservation.setClient(clientRepo.findById(reservationDto.getClientid()).get());

        return reservation;
    }



}

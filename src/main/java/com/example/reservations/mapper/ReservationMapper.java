package com.example.reservations.mapper;

import com.example.reservations.domain.Reservation;
import com.example.reservations.dto.ReservationDto;
import com.example.reservations.repositories.ClientRepo;
import com.example.reservations.repositories.StatutReservationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    @Autowired
    private StatutReservationRepo statutReservationRepo;

    @Autowired
    private ClientRepo clientRepo;

    public ReservationDto ToDTO(Reservation reservation) {
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
        reservationDto.setStatutId(reservation.getStatut().getId());
        reservationDto.setClientid(reservation.getClient().getId());

        return reservationDto;
    }


    public Reservation ToEntity(ReservationDto reservationDto) {
        if (reservationDto == null) {
            return null;
        }
        Reservation reservation = new Reservation();
        reservation.setDateDebut(reservationDto.getDateDebut());
        reservation.setDateFin(reservationDto.getDateFin());
        reservation.setPrixTotal(reservationDto.getPrixTotal());

        // Utilisation des repositories injectés pour récupérer le statut et le client
        reservation.setStatut(statutReservationRepo.findById(reservationDto.getStatutId())
                .orElseThrow(() -> new RuntimeException("Statut non trouvé")));
        reservation.setClient(clientRepo.findById(reservationDto.getClientid())
                .orElseThrow(() -> new RuntimeException("Client non trouvé")));

        return reservation;
    }
}

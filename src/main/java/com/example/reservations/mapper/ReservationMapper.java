package com.example.reservations.mapper;

import com.example.reservations.domain.Reservation;
import com.example.reservations.dto.ReservationDto;
import com.example.reservations.repositories.ClientRepo;
import com.example.reservations.repositories.StatutReservationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReservationMapper {

    // Retirer 'static' pour permettre l'injection via Spring
    @Autowired
    private StatutReservationRepo statutReservationRepo;

    @Autowired
    private ClientRepo clientRepo;

    // La méthode 'ToDTO' n'est plus statique
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

        return reservationDto;
    }

    // La méthode 'ToEntity' n'est plus statique
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
                .orElseThrow(() -> new RuntimeException("Statut non trouvé")));  // Ajouter gestion d'erreur si nécessaire
        reservation.setClient(clientRepo.findById(reservationDto.getClientid())
                .orElseThrow(() -> new RuntimeException("Client non trouvé")));  // Ajouter gestion d'erreur si nécessaire

        return reservation;
    }
}

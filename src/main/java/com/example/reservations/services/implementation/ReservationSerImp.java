package com.example.reservations.services.implementation;

import com.example.reservations.domain.Reservation;
import com.example.reservations.dto.ClientDto;
import com.example.reservations.dto.ReservationDto;
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
    private ReservationMapper reservationMapper;  // Injection de ReservationMapper

    @Override
    public ReservationDto create(ReservationDto reservationDto) {
        Reservation reservation = reservationMapper.ToEntity(reservationDto);  // Appel via l'instance injectée
        reservationRepo.save(reservation);
        return reservationMapper.ToDTO(reservation);  // Appel via l'instance injectée
    }

    @Override
    public List<ReservationDto> findAll() {
        return reservationRepo.findAll().stream()
                .map(reservationMapper::ToDTO)  // Appel via l'instance injectée
                .collect(Collectors.toList());
    }

    // Nouvelle méthode pour récupérer les réservations par statut et période
    public List<ReservationDto> findReservationsByStatutAndPeriod(String statut, LocalDate startDate, LocalDate endDate) {
        return reservationRepo.findReservationsByStatutAndPeriod(statut, startDate, endDate)
                .stream()
                .map(reservationMapper::ToDTO)  // Appel via l'instance injectée
                .collect(Collectors.toList());
    }

    // Nouvelle méthode pour calculer le revenu total par statut et période
    public Double calculateTotalRevenueByStatutAndPeriod(String statut, LocalDate startDate, LocalDate endDate) {
        return reservationRepo.calculateTotalRevenueByStatutAndPeriod(statut, startDate, endDate);
    }

    // Nouvelle méthode pour trouver les clients ayant réservé plus de X fois dans une année
    public List<ClientDto> ReservationsMore(int year, int x) {
        return reservationRepo.ReservationsMore(year, x)
                .stream()
                .map(ClientMapper::ToDTO)  // Appel via l'instance injectée de ClientMapper
                .collect(Collectors.toList());
    }
}

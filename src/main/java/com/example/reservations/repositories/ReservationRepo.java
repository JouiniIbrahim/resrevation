package com.example.reservations.repositories;

import com.example.reservations.domain.Client;
import com.example.reservations.domain.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation, Long> {

    // Récupérer toutes les réservations ayant un statut donné pour une période donnée
    @Query("SELECT r FROM Reservation r WHERE r.statut.libelle = :statut AND r.dateDebut >= :startDate AND r.dateFin <= :endDate")
    List<Reservation> findReservationsByStatutAndPeriod(
            @Param("statut") String statut,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // Calculer le revenu total des réservations ayant un statut spécifique pour une période donnée
    @Query("SELECT SUM(r.prixTotal) FROM Reservation r WHERE r.statut.libelle = :statut AND r.dateDebut >= :startDate AND r.dateFin <= :endDate")
    Double calculateTotalRevenueByStatutAndPeriod(
            @Param("statut") String statut,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // Trouver les clients qui ont réservé plus de X fois dans une année donnée
    @Query("SELECT r.client FROM Reservation r WHERE YEAR(r.dateDebut) = :year GROUP BY r.client HAVING COUNT(r) > :x")
    List<Client> ReservationsMore(
            @Param("year") int year,
            @Param("x") int x
    );
}
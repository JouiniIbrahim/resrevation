package com.example.reservations.repositories;

import com.example.reservations.domain.Client;
import com.example.reservations.domain.Reservation;

import com.example.reservations.dto.ClientDto;
import com.example.reservations.dto.ReservationDto;
import com.example.reservations.dto.ReservationResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation, Long> {


    // Retrieve all reservations having a given statut ID for a specific period.
    @Query("SELECT new com.example.reservations.dto.ReservationResponseDto(r.id , r.dateDebut,r.dateFin,r.prixTotal,r.statut.id,r.client.id)"+
            " FROM Reservation r WHERE r.statut.id = :statutId AND r.dateDebut >= :startDate AND r.dateFin <= :endDate")

    List<ReservationResponseDto> findReservationsByStatutAndPeriod(
            @Param("statutId") Long statutId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // Calculate the total revenue of reservations having a specific statut ID for a period.
    @Query("SELECT SUM(r.prixTotal) FROM Reservation r WHERE r.statut.id = :statutId AND r.dateDebut >= :startDate AND r.dateFin <= :endDate")
    Double TotalRevenue(
            @Param("statutId") Long statutId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    // Find clients who have reserved more than X times in a given year
    @Query("SELECT r.client FROM Reservation r WHERE YEAR(r.dateDebut) = :year GROUP BY r.client HAVING COUNT(r) > :x")
    List<Client> ReservationsMore(
            @Param("year") int year,
            @Param("x") int x
    );

    @Query("SELECT new com.example.reservations.dto.ReservationResponseDto(r.id, r.dateDebut,r.dateFin,r.prixTotal,r.statut.id,r.client.id) FROM Reservation r")
    List<ReservationResponseDto> allReservations();


}
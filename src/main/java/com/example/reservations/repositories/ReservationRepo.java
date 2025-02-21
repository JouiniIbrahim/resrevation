package com.example.reservations.repositories;

import com.example.reservations.domain.Client;
import com.example.reservations.domain.Reservation;
import com.example.reservations.dto.ReservationResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReservationRepo extends JpaRepository<Reservation, Long> {

    /**
     * Retrieve all reservations that have a given statut ID for a specific period.
     *
     * @param statutId  The ID of the statut (status) to filter the reservations by.
     * @param startDate The start date of the period to filter reservations.
     * @param endDate   The end date of the period to filter reservations.
     * @return A list of ReservationResponseDto objects containing reservation details within the specified period and with the specified statut ID.
     */
    @Query("SELECT new com.example.reservations.dto.ReservationResponseDto(r.id , r.dateDebut,r.dateFin,r.prixTotal,r.statut.id,r.client.id,r.messageFr)" +
            " FROM Reservation r WHERE r.statut.id = :statutId AND r.dateDebut >= :startDate AND r.dateFin <= :endDate")
    List<ReservationResponseDto> findReservationsByStatutAndPeriod(
            @Param("statutId") Long statutId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    /**
     * Calculate the total revenue from reservations that have a specific statut ID for a given period.
     *
     * @param statutId  The ID of the statut (status) to filter the reservations by.
     * @param startDate The start date of the period to calculate revenue for.
     * @param endDate   The end date of the period to calculate revenue for.
     * @return The total revenue as a Double.
     */
    @Query("SELECT SUM(r.prixTotal) FROM Reservation r WHERE r.statut.id = :statutId AND r.dateDebut >= :startDate AND r.dateFin <= :endDate")
    Double TotalRevenue(
            @Param("statutId") Long statutId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    /**
     * Find clients who have made more than X reservations in a given year.
     *
     * @param year The year to filter reservations by.
     * @param x    The minimum number of reservations a client should have made in the given year.
     * @return A list of clients who have made more than X reservations in the specified year.
     */
    @Query("SELECT r.client FROM Reservation r WHERE YEAR(r.dateDebut) = :year GROUP BY r.client HAVING COUNT(r) > :x")
    List<Client> ReservationsMore(
            @Param("year") int year,
            @Param("x") int x
    );

    /**
     * Retrieve all reservations in the system.
     *
     * @return A list of ReservationResponseDto objects containing details of all reservations.
     */
    @Query("SELECT new com.example.reservations.dto.ReservationResponseDto(r.id, r.dateDebut,r.dateFin,r.prixTotal,r.statut.id,r.client.id,r.messageFr) FROM Reservation r")
    List<ReservationResponseDto> allReservations();
/*    @Query("SELECT r FROM Reservation r WHERE r.client.id = ?1 AND r. = ?2")
    Reservation findByClientIdAndReservationId(Long clientId, Long reservationId);*/

}

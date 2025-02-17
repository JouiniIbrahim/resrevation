package com.example.reservations.repositories;

import com.example.reservations.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ClientRepo extends JpaRepository<Client, Long> {

    @Query(value = "SELECT * FROM client WHERE email = :email", nativeQuery = true)
    Client findByEmail(@Param("email") String email);

    // Récupérer la liste des clients inscrits après une date donnée
    List<Client> findByDateInscriptionAfter(LocalDateTime date);

    // Récupérer la liste des clients ayant effectué au moins une réservation
    @Query("SELECT DISTINCT c FROM Client c JOIN c.reservations r")
    List<Client> findClientsWithReservations();
}
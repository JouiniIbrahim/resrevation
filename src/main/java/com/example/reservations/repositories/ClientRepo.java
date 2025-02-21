package com.example.reservations.repositories;

import com.example.reservations.domain.Client;
import com.example.reservations.dto.ClientDto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepo extends JpaRepository<Client, Long> {
    /**
     * Find a client by email, returning a ClientDto wrapped in an Optional.
     *
     * @param email The email of the client to find.
     * @return An Optional containing the ClientDto if found, or empty if not.
     */

    @Query("SELECT new com.example.reservations.dto.ClientDto(c.id, c.nom, c.prenom, c.email, c.telephone, c.dateInscription,c.messageFr) " +
            "FROM Client c WHERE c.email = :email")
    Optional<ClientDto> findByEmail(@RequestBody String email);

    /**
     * Find clients who registered after a specified date.
     *
     * @param date The date to compare the client's registration date.
     * @return A list of Client objects who registered after the given date.
     */
    List<Client> findByDateInscriptionAfter(LocalDateTime date);

    /**
     * Find all clients who have made at least one reservation.
     *
     * @return A list of Client objects who have at least one reservation.
     */
    @Query("SELECT DISTINCT c FROM Client c JOIN c.reservations r")
    List<Client> findClientsWithReservations();

    /**
     * Find all clients, returning a list of ClientDto objects.
     *
     * @return A list of ClientDto objects representing all clients.
     */
    @Query("SELECT new com.example.reservations.dto.ClientDto(c.id, c.nom, c.prenom, c.email, c.telephone, c.dateInscription,c.messageFr) FROM Client c")
    List<ClientDto> findAllClients();

}



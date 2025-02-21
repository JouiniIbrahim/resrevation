package com.example.reservations.repositories;

import com.example.reservations.domain.StatutReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StatutReservationRepo extends JpaRepository<StatutReservation, Long> {

    Optional<StatutReservation> findByLibelle(String libelle);
}

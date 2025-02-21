package com.example.reservations.services.implementation;

import com.example.reservations.domain.Reservation;
import com.example.reservations.repositories.ReservationRepo;
import org.camunda.bpm.engine.delegate.DelegateExecution;
import org.camunda.bpm.engine.delegate.JavaDelegate;
import org.camunda.bpm.engine.task.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UpdateReservationStatus implements JavaDelegate {

    @Autowired
    private ReservationSerImp reservationSerImp;
    @Autowired
    private ReservationRepo reservationRepo;

    @Override
    public void execute(DelegateExecution execution) throws Exception {
        // Get the final status from process variables (true/false)
        Optional<Reservation> reservationProcess = Optional.ofNullable((Reservation) execution.getVariable("reservation"));
        Optional<Boolean> decision = Optional.ofNullable((Boolean) execution.getVariable("statut"));

        if (reservationProcess.isPresent() && decision.isPresent()) {
            Reservation reservation = reservationRepo.findById(reservationProcess.get().getId()).orElse(null);
            System.out.println("Reservatio  ");

            Boolean status = decision.get();

            reservationSerImp.updateReservationStatus(reservation, status);
            System.out.println("Updated reservation status for client ");

        }
    }
}

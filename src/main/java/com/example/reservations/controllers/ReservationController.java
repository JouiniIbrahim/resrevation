package com.example.reservations.controllers;

import com.example.reservations.domain.Client;
import com.example.reservations.dto.ClientDto;
import com.example.reservations.dto.ReservationDto;
import com.example.reservations.dto.ReservationResponseDto;
import com.example.reservations.dto.Response;
import com.example.reservations.services.implementation.ReservationSerImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private ReservationSerImp reservationSerImp;

    // Save a new reservation
    @PostMapping("/save")
    public ResponseEntity<Object> save(@RequestBody ReservationDto reservationDto) {
        ReservationDto savedReservation = reservationSerImp.create(reservationDto);
        String message = "Reservation created successfully.";
        return new ResponseEntity<>(new Response(savedReservation, message), HttpStatus.CREATED);
    }

    // Get all reservations
    @GetMapping("/all")
    public ResponseEntity<Object> allReservations() {
        List<ReservationResponseDto> reservations = reservationSerImp.findAll();
        if (reservations.isEmpty()) {
            String message = "No reservations found.";
            return new ResponseEntity<>(new Response(null, message), HttpStatus.NO_CONTENT); // 204 if no reservations
        }
        String message = "Reservations retrieved successfully.";
        return new ResponseEntity<>(new Response(reservations, message), HttpStatus.OK);
    }

    // Find reservations by status and period
    @GetMapping("/statut-period")
    public ResponseEntity<Object> findReservationsByStatutAndPeriod(
            /*@RequestParam Long statutId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate)*/
    @RequestBody ReservationDto reservationDto)
    {
        List<ReservationResponseDto> reservations = reservationSerImp.findReservations(
                reservationDto.getStatutId(),
                reservationDto.getDateDebut(),
                reservationDto.getDateFin());
        if (reservations.isEmpty()) {
            String message = "No reservations found for the specified period and status.";
            return new ResponseEntity<>(new Response(null, message), HttpStatus.NO_CONTENT); // 204 if no reservations found
        }
        String message = "Reservations retrieved successfully for the specified period and status.";
        return new ResponseEntity<>(new Response(reservations, message), HttpStatus.OK);
    }

    // Calculate total revenue by status and period
    @GetMapping("/total")
    public ResponseEntity<Object> calculateTotalRevenueByStatutAndPeriod(
            /*@RequestParam Long statutId,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate)*/
            @RequestBody ReservationDto reservationDto)
    {
        Double totalRevenue = reservationSerImp.TotalRevenue(
                reservationDto.getStatutId(),
                reservationDto.getDateDebut(),
                reservationDto.getDateFin());
        if (totalRevenue == null || totalRevenue == 0) {
            String message = "No revenue found for the specified period and status.";
            return new ResponseEntity<>(new Response(null, message), HttpStatus.NO_CONTENT); // 204 if no revenue
        }
        String message = "Total revenue calculated successfully.";
        return new ResponseEntity<>(new Response(totalRevenue, message), HttpStatus.OK);
    }

    // Find clients with more reservations
    @GetMapping("/reservations-more")
    public ResponseEntity<Object> ReservationsMore(
            @RequestParam int year,
            @RequestParam int x) {
        List<Client> clients = reservationSerImp.ReservationsMore(year, x);
        if (clients.isEmpty()) {
            String message = "No clients with more than the specified number of reservations found.";
            return new ResponseEntity<>(new Response(null, message), HttpStatus.NO_CONTENT); // 204 if no clients
        }
        String message = "Clients with more reservations retrieved successfully.";
        return new ResponseEntity<>(new Response(clients, message), HttpStatus.OK);
    }
}
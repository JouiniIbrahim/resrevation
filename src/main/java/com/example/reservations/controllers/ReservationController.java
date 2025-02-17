package com.example.reservations.controllers;

import com.example.reservations.dto.ClientDto;
import com.example.reservations.dto.ReservationDto;
import com.example.reservations.services.implementation.ReservationSerImp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    @Autowired
    private ReservationSerImp reservationSerImp;

    @PostMapping("/save")
    public ReservationDto save(@RequestBody ReservationDto reservationDto) {
        return reservationSerImp.create(reservationDto);
    }

    @GetMapping("/all")
    public List<ReservationDto> allReservations() {
        return reservationSerImp.findAll();
    }

    @GetMapping("/statut-period")
    public List<ReservationDto> findReservationsByStatutAndPeriod(
            @RequestParam String statut,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
//    @RequestBody ReservationDto reservationDto
    ){
        return reservationSerImp.findReservations(statut,startDate ,endDate );
    }
    /*@GetMapping("/statut-period")
    public List<ReservationDto> findReservationsByStatutAndPeriod(
            @RequestParam String statut,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate
//    @RequestBody ReservationDto reservationDto
    ){
        return reservationSerImp.findReservations(reservationDto.getStatut().getLibelle(),reservationDto.getDateDebut() ,reservationDto.getDateFin() );
    }*/

    @GetMapping("/total")
    public Double calculateTotalRevenueByStatutAndPeriod(
            @RequestParam String statut,
            @RequestParam LocalDate startDate,
            @RequestParam LocalDate endDate) {
        return reservationSerImp.TotalRevenue(statut, startDate, endDate);
    }

    @GetMapping("/reservations-more")
    public List<ClientDto> ReservationsMore(
            @RequestParam int year,
            @RequestParam int x) {
        return reservationSerImp.ReservationsMore(year, x);
    }
}
package com.example.reservations.controllers;

import com.example.reservations.domain.Reservation;
import com.example.reservations.dto.ReservationDto;
import com.example.reservations.services.implementation.ReservationSerImp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/reservation")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ReservationController {

    @Autowired
    private ReservationSerImp reservationSerImp;

    @PostMapping("/save")
    public ReservationDto save(@RequestBody ReservationDto reservationDto)
    {
        return reservationSerImp.create(reservationDto);
    }


    @GetMapping("/all")
    public List<ReservationDto> allReservations() {
        return reservationSerImp.findAll();
    }
}

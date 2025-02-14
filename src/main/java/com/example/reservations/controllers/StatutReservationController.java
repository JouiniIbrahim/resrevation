package com.example.reservations.controllers;


import com.example.reservations.domain.StatutReservation;
import com.example.reservations.dto.StatutReservationDto;
import com.example.reservations.services.implementation.StatutReservationSerImp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/status")
@CrossOrigin(origins = "*", maxAge = 3600)
public class StatutReservationController {

    @Autowired
    private StatutReservationSerImp statutReservationSerImp;

    @PostMapping("/save")
    public StatutReservationDto save(@RequestBody StatutReservationDto statutReservationDto) {
        return statutReservationSerImp.saveStatutReservation(statutReservationDto);
    }

    @GetMapping("/all")
    public List<StatutReservationDto> getAll() {
        return statutReservationSerImp.getAllStatutReservations();
    }
}

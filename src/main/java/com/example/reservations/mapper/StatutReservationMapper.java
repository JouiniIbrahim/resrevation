package com.example.reservations.mapper;


import com.example.reservations.domain.StatutReservation;

import com.example.reservations.dto.StatutReservationDto;

public class StatutReservationMapper {

    public static StatutReservationDto ToDTO(StatutReservation statutReservation) {
        if (statutReservation == null) {
            return null;
        }
        StatutReservationDto statutReservationDto = new StatutReservationDto();
        statutReservationDto.setId(statutReservation.getId());
        statutReservationDto.setLibelle(statutReservation.getLibelle());
        return statutReservationDto;
    }


    public static StatutReservation ToEntity(StatutReservationDto statutReservationDto) {
        if (statutReservationDto == null) {
            return null;
        }
        StatutReservation statutReservation = new StatutReservation();
        statutReservation.setId(statutReservationDto.getId());
        statutReservation.setLibelle(statutReservationDto.getLibelle());
        return statutReservation;
    }

}

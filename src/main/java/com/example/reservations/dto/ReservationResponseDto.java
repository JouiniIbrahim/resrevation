package com.example.reservations.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationResponseDto {

    private Long id;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Double prixTotal;
    //  @JsonIgnore
    private Long statutId;
    // @JsonIgnore
    private Long clientid;

}

package com.example.reservations.dto;

import com.example.reservations.domain.StatutReservation;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.camunda.bpm.engine.task.Task;

import java.time.LocalDate;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {

    private Long id;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Double prixTotal;
    //  @JsonIgnore
    private Long statutId;
    private StatutReservationDto statut;
    // @JsonIgnore
    private Long clientid;
    private ClientDto client;
    private String messageFr;

    private Double totalRevenue;
    private String taskId;
    private  String taskName;



}

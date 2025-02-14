package com.example.reservations.dto;

import com.example.reservations.domain.StatutReservation;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;



@AllArgsConstructor
@NoArgsConstructor
public class ReservationDto {

    private Long id;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Double prixTotal;
    private Long statutId;
    private StatutReservationDto statut;
    private Long clientid;
    private ClientDto client;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public Double getPrixTotal() {
        return prixTotal;
    }

    public void setPrixTotal(Double prixTotal) {
        this.prixTotal = prixTotal;
    }

    public Long getStatutId() {
        return statutId;
    }

    public void setStatutId(Long statutId) {
        this.statutId = statutId;
    }

    public Long getClientid() {
        return clientid;
    }

    public void setClientid(Long clientid) {
        this.clientid = clientid;
    }

    public ClientDto getClient() {
        return client;
    }

    public void setClient(ClientDto client) {
        this.client = client;
    }

    public StatutReservationDto getStatut() {
        return statut;
    }

    public void setStatut(StatutReservationDto statut) {
        this.statut = statut;
    }
}

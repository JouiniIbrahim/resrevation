package com.example.reservations.domain;


import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Reservation implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    private Double prixTotal;

    private String processInstId;

    @ManyToOne
    @JoinColumn(name = "statut_id", nullable = false)
    private StatutReservation statut;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;
    private String messageFr;

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", prixTotal=" + prixTotal +
                ", processInstId='" + processInstId + '\'' +
                ", statut=" + statut +
                ", client=" + client +
                ", messageFr='" + messageFr + '\'' +
                '}';
    }
}

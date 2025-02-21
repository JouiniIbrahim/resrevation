package com.example.reservations.dto;


import java.time.LocalDateTime;

public interface ResponseClientDto {

    Long getId();

    String getNom();

    String getPrenom();

    String getEmail();

    String getTelephone();

    LocalDateTime getDateInscription();

}

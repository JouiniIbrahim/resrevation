package com.example.reservations.repositories;

import com.example.reservations.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;



@Repository
public interface ClientRepo extends JpaRepository<Client, Long> {

    public Client findByEmail(String email);







    }



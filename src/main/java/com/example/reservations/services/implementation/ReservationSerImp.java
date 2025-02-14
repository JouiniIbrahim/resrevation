package com.example.reservations.services.implementation;


import com.example.reservations.domain.Reservation;
import com.example.reservations.dto.ReservationDto;
import com.example.reservations.mapper.ReservationMapper;
import com.example.reservations.repositories.ClientRepo;
import com.example.reservations.repositories.ReservationRepo;
import com.example.reservations.services.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationSerImp implements ReservationService {

    private static final Logger log = LoggerFactory.getLogger(ReservationSerImp.class);
    @Autowired
    ReservationRepo reservationRepo;
    @Autowired
    private ClientRepo clientRepo;
    @Autowired
    private ReservationMapper reservationMapper;

    @Override
    public ReservationDto create(ReservationDto reservationDto)
    {
        Reservation reservation = reservationMapper.ToEntity(reservationDto);

        log.info("Creating reservation: {}", reservationDto);
        log.info("Creating reservation-----------------------: {}", reservation);
        reservation.setClient(clientRepo.findById(reservationDto.getClientid()).get());

        reservationRepo.save(reservation);
        return reservationMapper.ToDTO(reservation);
    }
    @Override
    public List<ReservationDto> findAll()
    {
        return reservationRepo.findAll().stream().map(reservationMapper::ToDTO)
                .collect(Collectors.toList());
    }

}

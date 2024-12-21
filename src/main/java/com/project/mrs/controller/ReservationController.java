package com.project.mrs.controller;

import com.project.mrs.dto.APIResponseDTO;
import com.project.mrs.dto.reservation.ReservationRequestDTO;
import com.project.mrs.entity.Reservation;
import com.project.mrs.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    ReservationService reservationService;

    @Autowired
    ReservationController(ReservationService reservationService)
    {
        this.reservationService = reservationService;
    }

    @PostMapping("/reserve")
    public ResponseEntity<APIResponseDTO> createNewReservation(@RequestBody ReservationRequestDTO reservationRequestDTO)
    {
        Reservation newReservation = reservationService.createNewReservation(reservationRequestDTO);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        APIResponseDTO.builder()
                                .message("New Movie created with the id: " + newReservation.getReservationId() + " and movie name: "+newReservation.getShow().getMovie().getMovieName()+".")
                                .data(newReservation)
                                .build()
                );
    }

    @PutMapping("/cancel/{reservationId}")
    public ResponseEntity<APIResponseDTO> cancelReservation(@PathVariable Long reservationId)
    {

        String message = "The reservation Id : "+reservationId+" is already cancelled.";
        if(reservationService.cancelReservation(reservationId))
        {
            message = "Cancelled the reservation Id : "+reservationId+".";
        }

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        APIResponseDTO.builder()
                                .message(message)
                                .build()
                );
    }
}

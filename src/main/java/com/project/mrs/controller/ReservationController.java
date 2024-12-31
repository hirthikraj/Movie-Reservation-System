package com.project.mrs.controller;

import com.project.mrs.dto.APIResponseDTO;
import com.project.mrs.dto.PagedAPIResponseDTO;
import com.project.mrs.dto.reservation.ReservationRequestDTO;
import com.project.mrs.entity.Reservation;
import com.project.mrs.service.ReservationService;
import com.project.mrs.validation.UserRoleValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {

    private final ReservationService reservationService;
    private final UserRoleValidationService userRoleValidationService;

    @Autowired
    ReservationController(ReservationService reservationService, UserRoleValidationService userRoleValidationService)
    {
        this.reservationService = reservationService;
        this.userRoleValidationService = userRoleValidationService;
    }

    @GetMapping("/user/{userId}/all")
    public ResponseEntity<PagedAPIResponseDTO> getAllReservations(
            @PathVariable Long userId,
            @RequestParam int page,
            @RequestParam int pageSize
    )
    {
        Page<Reservation> reservations = reservationService.getAllReservationsForUser(userId,page,pageSize);
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(
                        PagedAPIResponseDTO
                                .builder()
                                .pageData(reservations.getContent())
                                .totalElements(reservations.getTotalElements())
                                .totalPages(reservations.getTotalPages())
                                .currentLimit(reservations.getNumberOfElements())
                                .build()
                );
    }

    @Secured("ROLE_USER")
    @PostMapping("/reserve")
    public ResponseEntity<APIResponseDTO> createNewReservation(@RequestBody ReservationRequestDTO reservationRequestDTO)
    {
        // Need to verify if the current user is trying to create a reservation for another user
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

    @PreAuthorize("@userRoleValidationService.isUserHavePermissionToCancelReservation(#reservationId)")
    @PutMapping("/cancel/{reservationId}")
    public ResponseEntity<APIResponseDTO> cancelReservation(@PathVariable Long reservationId)
    {
        // Need to verify if the current user have permission to cancel the reservation.

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

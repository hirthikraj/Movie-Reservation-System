package com.project.mrs.service;

import com.project.mrs.constants.ExceptionConstants;
import com.project.mrs.dto.reservation.ReservationRequestDTO;
import com.project.mrs.dto.showSeat.ShowSeatRequestDTO;
import com.project.mrs.entity.*;
import com.project.mrs.enums.ReservationStatus;
import com.project.mrs.exception.AvailableShowSeatsNotFoundException;
import com.project.mrs.exception.ReservationNotCancellableException;
import com.project.mrs.exception.ReservationNotFoundException;
import com.project.mrs.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

import static com.project.mrs.constants.ExceptionConstants.AVAILABLE_SHOW_SEATS_NOT_FOUND;
import static com.project.mrs.constants.ExceptionConstants.RESERVATION_NOT_CANCELLABLE;

@Service
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final UserService userService;
    private final  ShowService showService;
    private final TheatreService theatreService;

    @Autowired
    ReservationService(ReservationRepository reservationRepository,ShowService showService,UserService userService,TheatreService theatreService)
    {
        this.reservationRepository = reservationRepository;
        this.showService = showService;
        this.userService = userService;
        this.theatreService = theatreService;
    }

    public Page<Reservation> getAllReservationsForUser(Long userId,int page, int pageSize)
    {
        return reservationRepository.findByUser_UserId(userId,PageRequest.of(page,pageSize));
    }

    public Reservation getReservationById(Long reservationId)
    {
        return reservationRepository.findById(reservationId).orElseThrow(() -> new ReservationNotFoundException(ExceptionConstants.RESERVATION_NOT_FOUND,HttpStatus.NOT_FOUND));
    }

    public Reservation createNewReservation(ReservationRequestDTO reservationRequestDTO)
    {
        List<Long> showSeatIds = reservationRequestDTO.getShowSeats().stream().map(ShowSeatRequestDTO::getSeatId).toList();
        List<ShowSeat> showSeats = showService.showSeatService.getAvailableSeats(showSeatIds);
        showService.showSeatService.acquireLocksForShowSeats(showSeatIds);
        if(showSeats.size() != showSeatIds.size())
        {
            showService.showSeatService.removeLocksForShowSeats(showSeatIds);
            throw new AvailableShowSeatsNotFoundException(AVAILABLE_SHOW_SEATS_NOT_FOUND, HttpStatus.NOT_FOUND);
        }
        User user = userService.getUserById(reservationRequestDTO.getUserId());
        Show show = showService.getShowById(reservationRequestDTO.getShowId());
        double totalAmount = showService.showSeatService.bookSeatsAndReturnTotalAmount(showSeats);

        Theatre theatre = show.getTheatre();
        theatre.setTotalRevenue(theatre.getTotalRevenue() + totalAmount);
        theatre.setTotalBookings(theatre.getTotalBookings() + showSeats.size());
        theatreService.updateTheatre(theatre);

        Reservation reservation = Reservation
                                    .builder()
                                    .show(show)
                                    .user(user)
                                    .seatsReserved(showSeats)
                                    .reservationTime(LocalDateTime.now())
                                    .updatedTime(LocalDateTime.now())
                                    .reservationStatus(ReservationStatus.BOOKED)
                                    .totalAmount(totalAmount)
                                    .build();

        showService.showSeatService.removeLocksForShowSeats(showSeatIds);
        return reservationRepository.save(reservation);
    }

    public boolean cancelReservation(Long reservationId)
    {
        Reservation reservation = getReservationById(reservationId);
        if(reservation.getReservationStatus() == ReservationStatus.BOOKED)
        {
            if(!isReservationCancellable(reservation.getShow()))
            {
                throw new ReservationNotCancellableException(RESERVATION_NOT_CANCELLABLE,HttpStatus.NOT_ACCEPTABLE);
            }
            reservation.setReservationStatus(ReservationStatus.CANCELLED);
            reservation.setUpdatedTime(LocalDateTime.now());

            Theatre theatre = reservation.getShow().getTheatre();
            theatre.setTotalRevenue(theatre.getTotalRevenue() - reservation.getTotalAmount());
            theatre.setTotalBookings(theatre.getTotalBookings() + reservation.getSeatsReserved().size());
            theatreService.updateTheatre(theatre);

            reservationRepository.save(reservation);
            return true;
        }
        return false;
    }

    public boolean isReservationCancellable(Show show)
    {
        return show.getStartTime().isAfter(LocalDateTime.now().minusMinutes(30));
    }
}

package com.project.mrs.service;

import com.project.mrs.constants.ExceptionConstants;
import com.project.mrs.entity.Seat;
import com.project.mrs.entity.Show;
import com.project.mrs.entity.ShowSeat;
import com.project.mrs.enums.SeatStatus;
import com.project.mrs.exception.SeatAlreadyLockedException;
import com.project.mrs.lock.SeatLock;
import com.project.mrs.repository.ShowSeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class ShowSeatService {

    ShowSeatRepository showSeatRepository;
    SeatLock seatLock;

    @Autowired
    ShowSeatService(ShowSeatRepository showSeatRepository,SeatLock seatLock)
    {
        this.showSeatRepository = showSeatRepository;
        this.seatLock = seatLock;
    }

    public ShowSeat createNewShowSeat(Show show, Seat seat)
    {
        ShowSeat showSeat = ShowSeat
                                .builder()
                                .seat(seat)
                                .seatStatus(SeatStatus.AVAILABLE)
                                .show(show)
                                .build();

        return showSeatRepository.save(showSeat);
    }

    public List<ShowSeat> getShowSeats(List<Long> showSeatIds)
    {
        return showSeatRepository.findAllById(showSeatIds);
    }

    public List<ShowSeat> getAvailableSeats(List<Long> showSeatIds)
    {
        return showSeatRepository.findAllById(showSeatIds).stream().filter(showSeat -> showSeat.getSeatStatus().equals(SeatStatus.AVAILABLE)).toList();
    }

    public Double bookSeatsAndReturnTotalAmount(List<ShowSeat> showSeats)
    {
        Double amount = 0D;
        for(ShowSeat showSeat : showSeats)
        {
            showSeat.setSeatStatus(SeatStatus.BOOKED);
            amount += showSeat.getSeat().getSeatPrice();
        }
        return amount;
    }

    public void acquireLocksForShowSeats(List<Long> showSeatIds)
    {
        for(Long showSeatId : showSeatIds)
        {
            ReentrantLock reentrantLock = seatLock.getShowSeatLock(showSeatId);
            if(!reentrantLock.tryLock())
            {
                throw new SeatAlreadyLockedException(ExceptionConstants.SEAT_ALREADY_LOCKED, HttpStatus.CONFLICT);
            }
        }
    }

    public void removeLocksForShowSeats(List<Long> showSeatIds)
    {
        for(Long showSeatId : showSeatIds)
        {
            seatLock.removeLockForShowSeat(showSeatId);
        }
    }
}

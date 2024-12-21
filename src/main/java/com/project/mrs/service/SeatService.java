package com.project.mrs.service;

import com.project.mrs.dto.seat.SeatRequestDTO;
import com.project.mrs.entity.Screen;
import com.project.mrs.entity.Seat;
import com.project.mrs.enums.SeatType;
import com.project.mrs.repository.SeatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeatService {
    SeatRepository seatRepository;

    @Autowired
    SeatService(SeatRepository seatRepository)
    {
        this.seatRepository = seatRepository;
    }

    public Seat createNewSeat(Screen screen,SeatRequestDTO seatRequestDTO)
    {
        Seat seat = Seat
                .builder()
                .rowId(seatRequestDTO.getRowId())
                .seatNumber(seatRequestDTO.getSeatNumber())
                .seatPrice(seatRequestDTO.getSeatPrice())
                .seatType(SeatType.valueOf(seatRequestDTO.getSeatType()))
                .screen(screen)
                .build();

        return seatRepository.save(seat);
    }
}

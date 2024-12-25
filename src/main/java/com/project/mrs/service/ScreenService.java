package com.project.mrs.service;

import com.project.mrs.constants.ExceptionConstants;
import com.project.mrs.dto.screen.ScreenRequestDTO;
import com.project.mrs.dto.seat.SeatRequestDTO;
import com.project.mrs.entity.Screen;
import com.project.mrs.entity.Seat;
import com.project.mrs.entity.Theatre;
import com.project.mrs.exception.ScreenNotFoundException;
import com.project.mrs.repository.ScreenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ScreenService {

    private final ScreenRepository screenRepository;
    private final SeatService seatService;

    @Autowired
    ScreenService(ScreenRepository screenRepository,SeatService seatService)
    {
        this.screenRepository = screenRepository;
        this.seatService = seatService;
    }

    public Screen getScreenById(Long screenId)
    {
        return screenRepository
                .findById(screenId)
                .orElseThrow(() -> new ScreenNotFoundException(ExceptionConstants.SCREEN_NOT_FOUND, HttpStatus.NOT_FOUND));
    }

    public Screen createNewScreen(Theatre theatre, ScreenRequestDTO screenRequestDTO)
    {
        Screen screen = Screen
                .builder()
                .screenName(screenRequestDTO.getScreenName())
                .theatre(theatre)
                .build();

        List<Seat> seats = new ArrayList<>();

        for(SeatRequestDTO seatRequestDTO : screenRequestDTO.getSeats())
        {
            Seat seat = seatService.createNewSeat(screen,seatRequestDTO);
            seats.add(seat);
        }
        screen.setSeats(seats);
        return screenRepository.save(screen);
    }
}

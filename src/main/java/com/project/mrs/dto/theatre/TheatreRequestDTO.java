package com.project.mrs.dto.theatre;

import com.project.mrs.dto.screen.ScreenRequestDTO;
import com.project.mrs.dto.seat.SeatRequestDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class TheatreRequestDTO {
    private String theatreName;
    private String theatreLocation;
    List<ScreenRequestDTO> screens;
}

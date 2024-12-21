package com.project.mrs.dto.screen;

import com.project.mrs.dto.seat.SeatRequestDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ScreenRequestDTO {
    private String screenName;
    List<SeatRequestDTO> seats;
}

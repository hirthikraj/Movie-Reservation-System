package com.project.mrs.dto.reservation;

import com.project.mrs.dto.showSeat.ShowSeatRequestDTO;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class ReservationRequestDTO {

    private Long userId;
    private Long showId;
    private List<ShowSeatRequestDTO> showSeats;
}

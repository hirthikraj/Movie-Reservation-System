package com.project.mrs.dto.seat;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SeatRequestDTO {
    Integer rowId;

    Integer seatNumber;

    String seatType;

    Double seatPrice;
}

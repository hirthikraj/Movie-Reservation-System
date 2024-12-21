package com.project.mrs.entity;

import com.project.mrs.enums.SeatStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class ShowSeat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long showSeatId;

    @Enumerated(value = EnumType.STRING)
    private SeatStatus seatStatus;

    @ManyToOne()
    @JoinColumn(name = "seat_id")
    private Seat seat;

    @ManyToOne()
    @JoinColumn(name = "show_id")
    private Show show;
}

package com.project.mrs.entity;

import com.project.mrs.enums.Genre;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Movie {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long movieId;

    private String movieName;

    @Enumerated(value = EnumType.STRING)
    private Genre movieGenre;

    private String movieDirector;

    private LocalDate movieReleaseDate;

    private String movieDescription;

    private Long movieDuration;

    private Integer totalBookings;

    @OneToMany(mappedBy = "movie",cascade = CascadeType.ALL)
    List<Show> shows;
}

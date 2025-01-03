package com.project.mrs.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Theatre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long theatreId;

    private String theatreName;

    private String theatreLocation;

    private Integer totalScreens;

    private Integer totalBookings;

    private Double totalRevenue;

    @OneToMany(mappedBy = "theatre", cascade = CascadeType.ALL)
    private List<TheatreVsAdmin> theatreAdmins;

    @OneToMany(mappedBy = "theatre",cascade = CascadeType.ALL)
    List<Show> shows;

    @OneToMany(mappedBy = "theatre",cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    List<Screen> screens;
}

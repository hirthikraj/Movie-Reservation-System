package com.project.mrs.dto.theatre;

import com.project.mrs.dto.screen.ScreenRequestDTO;
import com.project.mrs.entity.User;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class TheatreRequestDTO {
    private String theatreName;
    private String theatreLocation;
    private Long theatreAdminId;

    List<ScreenRequestDTO> screens;
}

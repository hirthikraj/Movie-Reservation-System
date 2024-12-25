package com.project.mrs.dto.user;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class TheatreAdminRequestDTO {
    Long userId;
    Long theatreId;
}

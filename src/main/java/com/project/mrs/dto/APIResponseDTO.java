package com.project.mrs.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class APIResponseDTO {
    String message;
    Object data;
}

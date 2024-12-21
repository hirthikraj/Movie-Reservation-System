package com.project.mrs.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PagedAPIResponseDTO {
    List<?> pageData;
    long totalElements;
    int totalPages;
    int currentLimit;
}

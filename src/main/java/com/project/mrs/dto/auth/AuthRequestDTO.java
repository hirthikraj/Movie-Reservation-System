package com.project.mrs.dto.auth;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthRequestDTO {
    String userName;
    String password;
}

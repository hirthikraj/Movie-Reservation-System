package com.project.mrs.dto.user;

import com.project.mrs.enums.UserStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Builder
@Data
public class UserResponseDTO {
    private Long userId;
    private String userName;
    private String firstName;
    private String lastName;
    private String userEmail;
    private UserStatus userStatus;
    private LocalDateTime userCreatedAt;
    private LocalDateTime userUpdatedAt;
}

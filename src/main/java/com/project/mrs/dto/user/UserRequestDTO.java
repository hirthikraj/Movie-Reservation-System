package com.project.mrs.dto.user;
import lombok.Builder;
import lombok.Data;


@Builder
@Data
public class UserRequestDTO {
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private String userEmail;
}

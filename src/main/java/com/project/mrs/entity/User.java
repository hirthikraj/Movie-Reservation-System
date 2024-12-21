package com.project.mrs.entity;

import com.project.mrs.enums.UserStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
@Table( name = "app_users",
        uniqueConstraints = {
        @UniqueConstraint(columnNames = "userEmail")
})

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String userName;

    private String userPassword;

    private String firstName;

    private String lastName;

    private String userEmail;

    @Enumerated(value = EnumType.STRING)
    private UserStatus userStatus;

    private LocalDateTime userCreatedAt;

    private LocalDateTime userUpdatedAt;

}

package site.secmega.secapi.feature.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import site.secmega.secapi.base.UserStatus;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String username,
        String employee_id,
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        String role,
        String avatar,
        UserStatus status,
        @JsonFormat(pattern = "dd/MMM/yyyy hh:mma")
        LocalDateTime lastLogin
) {
}

package site.secmega.secapi.feature.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import site.secmega.secapi.base.UserStatus;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String username,
        String employeeId,
        String firstName,
        String lastName,
        String phoneNumber,
        String email,
        Integer roleId,
        String role,
        String avatar,
        UserStatus status,
        String line,
        Long lineId,
        String department,
        @JsonFormat(pattern = "dd/MMM/yyyy hh:mma")
        LocalDateTime lastLogin
) {
}

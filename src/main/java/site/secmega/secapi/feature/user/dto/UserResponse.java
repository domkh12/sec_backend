package site.secmega.secapi.feature.user.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import site.secmega.secapi.base.UserStatus;

import java.time.LocalDateTime;

public record UserResponse(
        Long id,
        String username,
        String employeeId,
        String nameEn,
        String nameKh,
        String phoneNumber,
        String email,
        Integer roleId,
        String role,
        String avatar,
        UserStatus status,
        String line,
        Long lineId,
        String department,
        Long departmentId,
        String position,
        @JsonFormat(pattern = "dd/MMM/yyyy hh:mma")
        LocalDateTime lastLogin
) {
}

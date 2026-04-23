package site.secmega.secapi.feature.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record UserRequest(
        String username,
        String password,
        String employeeId,
        String phoneNumber,
        @NotNull(message = "Role ID cannot be null")
        Long roleId,
        Long departmentId,
        Long lineId,
        String nameEn,
        String nameKh,
        String position,
        String gender
) {
}

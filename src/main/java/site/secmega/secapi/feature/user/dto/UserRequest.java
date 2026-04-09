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
        @Email(message = "Invalid email format")
        String email,
        Long departmentId,
        Long lineId,
        String firstName,
        String lastName,
        String nameEn,
        String nameKh,
        String position
) {
}

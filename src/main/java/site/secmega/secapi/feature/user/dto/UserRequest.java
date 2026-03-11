package site.secmega.secapi.feature.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record UserRequest(
        String username,
        String password,
        String employeeId,
        String phoneNumber,
        @NotBlank(message = "Role ID cannot be blank")
        Long roleId,
        @Email(message = "Invalid email format")
        String email
) {
}

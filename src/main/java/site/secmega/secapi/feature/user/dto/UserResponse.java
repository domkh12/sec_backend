package site.secmega.secapi.feature.user.dto;

public record UserResponse(
        String username,
        String employee_id,
        String phoneNumber
) {
}

package site.secmega.secapi.feature.user.dto;

public record UserRequest(
        String username,
        String password,
        String employee_id,
        String phoneNumber,
        Long roleId
) {
}

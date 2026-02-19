package site.secmega.secapi.feature.user.dto;

public record UserRequest(
        String username,
        String password,
        String employee_id,
        Integer phoneNumber,
        Long roleId
) {
}

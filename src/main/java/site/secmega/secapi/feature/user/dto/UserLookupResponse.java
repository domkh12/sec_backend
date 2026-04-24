package site.secmega.secapi.feature.user.dto;

public record UserLookupResponse(
        Long id,
        String employeeId,
        String nameEn,
        String nameKh,
        String avatar
) {
}

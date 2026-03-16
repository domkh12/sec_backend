package site.secmega.secapi.feature.role.dto;

public record RoleFilterRequest(
        Integer pageNo,
        Integer pageSize,
        String search
) {
}

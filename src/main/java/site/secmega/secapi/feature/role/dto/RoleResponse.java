package site.secmega.secapi.feature.role.dto;

public record RoleResponse(
        Integer id,
        String name,
        Integer users,
        String description
) {
}

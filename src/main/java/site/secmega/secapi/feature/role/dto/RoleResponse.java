package site.secmega.secapi.feature.role.dto;

import lombok.Builder;

@Builder
public record RoleResponse(
        Integer id,
        String name,
        Integer users,
        String description
) {
}

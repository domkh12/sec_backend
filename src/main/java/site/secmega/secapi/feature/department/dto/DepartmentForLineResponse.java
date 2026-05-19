package site.secmega.secapi.feature.department.dto;

import lombok.Builder;

@Builder
public record DepartmentForLineResponse(
        Long id,
        String name,
        Integer processNo
) {
}

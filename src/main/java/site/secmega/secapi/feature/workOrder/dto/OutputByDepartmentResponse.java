package site.secmega.secapi.feature.workOrder.dto;

import lombok.Builder;

@Builder
public record OutputByDepartmentResponse(
        Long id,
        String name,
        Integer output
) {
}

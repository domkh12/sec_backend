package site.secmega.secapi.feature.productionLine.dto;

import lombok.Builder;
import site.secmega.secapi.feature.department.dto.DepartmentForLineResponse;

@Builder
public record ProductionLineLookupResponse(
    Long id,
    String line,
    String image,
    Boolean isInput,
    DepartmentForLineResponse department
) {
}

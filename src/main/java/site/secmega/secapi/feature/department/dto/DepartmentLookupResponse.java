package site.secmega.secapi.feature.department.dto;

import lombok.Builder;
import site.secmega.secapi.feature.productionLine.dto.ProductionLineLookupResponse;

import java.util.List;

@Builder
public record DepartmentLookupResponse(
        Long id,
        String name,
        List<ProductionLineLookupResponse> lines
) {
}

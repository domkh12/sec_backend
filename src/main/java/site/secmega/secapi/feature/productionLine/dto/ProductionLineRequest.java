package site.secmega.secapi.feature.productionLine.dto;

import jakarta.validation.constraints.NotNull;

public record ProductionLineRequest(
        String line,
        @NotNull(message = "Department ID cannot be null")
        Long deptId
) {
}

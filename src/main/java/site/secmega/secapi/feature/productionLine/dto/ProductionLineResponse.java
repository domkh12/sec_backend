package site.secmega.secapi.feature.productionLine.dto;

import lombok.Builder;

@Builder
public record ProductionLineResponse(
        Long id,
        String line,
        String dept,
        Long deptId,
        Integer workers,
        Integer target,
        Integer actual,
        Double efficiency
) {
}

package site.secmega.secapi.feature.department.dto;

import lombok.Builder;

@Builder
public record DepartmentStatsResponse(
        Integer totalDept,
        Integer totalLine,
        Integer totalWorker,
        Integer totalActive
) {
}

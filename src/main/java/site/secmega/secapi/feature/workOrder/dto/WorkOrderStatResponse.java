package site.secmega.secapi.feature.workOrder.dto;

import lombok.Builder;

@Builder
public record WorkOrderStatResponse(
        long totalMO,
        long totalWorkOrderQty,
        long totalOutput,
        long totalBalance
) {
}

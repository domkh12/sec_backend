package site.secmega.secapi.feature.workOrder.dto;

import lombok.Builder;

@Builder
public record WorkOrderLookupResponse(
        Long id,
        String mo
) {
}

package site.secmega.secapi.feature.workOrderColorSize.dto;

import lombok.Builder;

@Builder
public record WorkOrderColorSizeResponse(
        String sizeName,
        Integer qty
) {
}

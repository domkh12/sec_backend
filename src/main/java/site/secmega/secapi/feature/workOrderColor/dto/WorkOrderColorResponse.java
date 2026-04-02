package site.secmega.secapi.feature.workOrderColor.dto;

import lombok.Builder;
import site.secmega.secapi.feature.workOrderColorSize.dto.WorkOrderColorSizeResponse;

import java.util.List;

@Builder
public record WorkOrderColorResponse(
        String colorName,
        List<WorkOrderColorSizeResponse> sizes
) {
}

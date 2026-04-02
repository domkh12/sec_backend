package site.secmega.secapi.feature.workOrder.dto;

import lombok.Builder;
import site.secmega.secapi.feature.color.dto.ColorLookupResponse;
import site.secmega.secapi.feature.workOrderColor.dto.WorkOrderColorResponse;

import java.util.List;

@Builder
public record WorkOrderResponse(
        String mo,
        Integer qty,
        String buyer,
        List<WorkOrderColorResponse> colors
) {
}

package site.secmega.secapi.feature.workOrder.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import site.secmega.secapi.base.WorkOrderStatus;
import site.secmega.secapi.feature.workOrderColor.dto.WorkOrderColorResponse;

import java.time.LocalDate;
import java.util.List;

@Builder
public record WorkOrderResponse(
        Long id,
        String mo,
        String style,
        Integer qty,
        String buyer,
        WorkOrderStatus status,
        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate startDate,
        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate endDate,
        String image
) {
}

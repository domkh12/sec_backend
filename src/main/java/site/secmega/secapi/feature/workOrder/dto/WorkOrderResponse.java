package site.secmega.secapi.feature.workOrder.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import site.secmega.secapi.base.WorkOrderStatus;
import site.secmega.secapi.feature.buyer.dto.BuyerLookupResponse;
import site.secmega.secapi.feature.color.dto.ColorLookupResponse;
import site.secmega.secapi.feature.size.dto.SizeLookupResponse;
import site.secmega.secapi.feature.workOrderColor.dto.WorkOrderColorResponse;

import java.time.LocalDate;
import java.util.List;

@Builder
public record WorkOrderResponse(
        Long id,
        String mo,
        String po,
        String style,
        Integer qty,
        Integer output,
        Integer balance,
        List<SizeLookupResponse> sizes,
        ColorLookupResponse color,
        WorkOrderStatus status,
        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate startDate,
        @JsonFormat(pattern = "dd-MM-yyyy")
        LocalDate endDate,
        String image,
        Boolean isActive
) {
}

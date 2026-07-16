package site.secmega.secapi.feature.defectDetail.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import site.secmega.secapi.feature.buyer.dto.BuyerLookupResponse;
import site.secmega.secapi.feature.productionLine.dto.ProductionLineLookupResponse;
import site.secmega.secapi.feature.purchaseOrder.dto.PurchaseOrderLookupResponse;
import site.secmega.secapi.feature.style.dto.StyleLookupResponse;
import site.secmega.secapi.feature.time.dto.TimeResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record DefectDetailResponse(
        Long id,
        @JsonFormat(pattern = "dd/MMM/yyyy hh:mma")
        LocalDateTime reportDate,
        String mo,
        Integer defectQty,
        LocalDate defectDate,
        TimeResponse time,
        ProductionLineLookupResponse line,
        PurchaseOrderLookupResponse purchaseOrder,
        StyleLookupResponse style,
        BuyerLookupResponse buyer
) {
}

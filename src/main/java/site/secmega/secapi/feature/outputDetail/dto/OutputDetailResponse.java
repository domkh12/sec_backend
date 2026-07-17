package site.secmega.secapi.feature.outputDetail.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;
import site.secmega.secapi.domain.PurchaseOrder;
import site.secmega.secapi.feature.buyer.dto.BuyerLookupResponse;
import site.secmega.secapi.feature.productionLine.dto.ProductionLineLookupResponse;
import site.secmega.secapi.feature.purchaseOrder.dto.PurchaseOrderLookupResponse;
import site.secmega.secapi.feature.size.dto.SizeLookupResponse;
import site.secmega.secapi.feature.style.dto.StyleLookupResponse;
import site.secmega.secapi.feature.time.dto.TimeResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record OutputDetailResponse(
        Long id,
        @JsonFormat(pattern = "dd/MMM/yyyy hh:mma")
        LocalDateTime reportDate,
        String mo,
        SizeLookupResponse size,
        Integer qty,
        LocalDate outputDate,
        TimeResponse time,
        ProductionLineLookupResponse line,
        PurchaseOrderLookupResponse purchaseOrder,
        StyleLookupResponse style,
        BuyerLookupResponse buyer
) {
}

package site.secmega.secapi.feature.purchaseOrder.dto;

import site.secmega.secapi.feature.style.dto.StyleResponse;

import java.time.LocalDate;

public record PurchaseOrderResponse(
        Long id,
        StyleResponse style,
        String po,
        Integer qty,
        LocalDate shipmentDate
) {
}

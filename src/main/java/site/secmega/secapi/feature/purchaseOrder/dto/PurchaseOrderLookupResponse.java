package site.secmega.secapi.feature.purchaseOrder.dto;

import lombok.Builder;

@Builder
public record PurchaseOrderLookupResponse(
        Long id,
        String po
) {
}

package site.secmega.secapi.feature.receipt.dto;

import site.secmega.secapi.feature.purchaseOrder.dto.PurchaseOrderLookupResponse;
import site.secmega.secapi.feature.supplier.dto.SupplierLookupResponse;

import java.time.LocalDateTime;

public record ReceiptResponse(
        Long id,
        String uuid,
        String receiptNo,
        LocalDateTime receiptDate,
        Double totalQty,
        String remark,
        PurchaseOrderLookupResponse purchaseOrder,
        SupplierLookupResponse supplier
) {
}

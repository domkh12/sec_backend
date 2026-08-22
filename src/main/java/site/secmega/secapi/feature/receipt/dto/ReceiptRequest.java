package site.secmega.secapi.feature.receipt.dto;

import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;

public record ReceiptRequest(
        String receiptNo,
        LocalDateTime receiptDate,
        String remark,
        @Min(value = 0, message = "Must positive!")
        Double totalQty,
        String poUuid,
        String supplierUuid
) {
}

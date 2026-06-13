package site.secmega.secapi.feature.material.dto;

import java.time.LocalDateTime;

public record UpdateStockInQtyRequest(
        Double qty,
        LocalDateTime transactionDate
) {
}

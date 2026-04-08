package site.secmega.secapi.feature.material.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record StockInRequest(
        @NotNull(message = "Material ID cannot be null")
        Long materialId,
        @NotNull(message = "Quantity cannot be null")
        Double qtyInput,
        @NotNull(message = "Date cannot be null")
        LocalDateTime dateInput
) {
}

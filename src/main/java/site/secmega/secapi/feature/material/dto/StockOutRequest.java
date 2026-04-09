package site.secmega.secapi.feature.material.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record StockOutRequest(
        @NotNull(message = "Material ID cannot be null")
        Long materialId,
        @NotNull(message = "Quantity cannot be null")
        Double qtyOutput,
        @NotNull(message = "Date cannot be null")
        LocalDateTime dateOutput,
        @NotNull(message = "Requester ID cannot be null")
        Long requesterId
) {
}

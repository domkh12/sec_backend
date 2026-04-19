package site.secmega.secapi.feature.workOrder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public record WorkOrderRequest(
        @NotBlank(message = "MO is required")
        String mo,
        @NotNull(message = "Quantity cannot be null")
        Integer qty,
        @NotNull(message = "Buyer ID cannot be null")
        Long buyerId,
        @NotBlank(message = "Style is required")
        String style,
        @NotNull(message = "Product ID cannot be null")
        Long colorId,
        @NotNull(message = "Size ID cannot be null")
        List<Long> sizeIds,
        @NotNull(message = "Start Date cannot be null")
        LocalDate startDate,
        @NotNull(message = "End Date ID cannot be null")
        LocalDate endDate,
        String image
) {
}

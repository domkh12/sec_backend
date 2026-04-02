package site.secmega.secapi.feature.workOrder.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record WorkOrderRequest(
        @NotBlank(message = "MO is required")
        String mo,
        @NotNull(message = "Quantity cannot be null")
        Integer qty,
        @NotNull(message = "Buyer ID cannot be null")
        Long buyerId,

        List<Integer> colorIds
) {
}

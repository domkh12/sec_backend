package site.secmega.secapi.feature.workOrder.dto;

import jakarta.validation.constraints.NotNull;

public record WorkOrderStatusRequest(
        @NotNull(message = "isActive value is required")
        Boolean isActive
) {
}

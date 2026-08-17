package site.secmega.secapi.feature.rack.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RackRequest(
    @NotBlank(message = "Code is required")
    String code,
    @NotNull(message = "Active is required")
    Boolean isActive,
    @NotBlank(message = "Warehouse uuid is required!")
    String warehouseUuid
) {
}

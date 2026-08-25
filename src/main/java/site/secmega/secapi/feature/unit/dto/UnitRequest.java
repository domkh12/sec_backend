package site.secmega.secapi.feature.unit.dto;

import jakarta.validation.constraints.NotBlank;

public record UnitRequest(
    @NotBlank(message = "unit code is required")
    String unitCode,
    @NotBlank(message = "unit name is required")
    String unitName,
    String description
) {
}

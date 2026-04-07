package site.secmega.secapi.feature.material.dto;

import jakarta.validation.constraints.NotBlank;

public record MaterialRequest(
        @NotBlank(message = "Code is required")
        String code,
        @NotBlank(message = "Name is required")
        String name,
        @NotBlank(message = "Unit is required")
        String unit,
        String image
) {
}

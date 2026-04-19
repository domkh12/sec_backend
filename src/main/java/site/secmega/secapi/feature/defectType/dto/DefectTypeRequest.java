package site.secmega.secapi.feature.defectType.dto;

import jakarta.validation.constraints.NotBlank;

public record DefectTypeRequest(
        @NotBlank(message = "Name is required")
        String name
) {
}

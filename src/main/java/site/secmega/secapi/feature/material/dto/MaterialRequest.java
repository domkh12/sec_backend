package site.secmega.secapi.feature.material.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record MaterialRequest(
        @NotBlank(message = "Code is required")
        String code,
        @NotBlank(message = "Name is required")
        String name,
        String description,
        @NotBlank(message = "Unit is required")
        String unit,
        String image,
        Long sizeId,
        Long colorId,
        List<Long> styleIds
) {
}

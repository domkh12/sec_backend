package site.secmega.secapi.feature.category.dto;

import jakarta.validation.constraints.NotBlank;

public record CategoryRequest(
    @NotBlank(message = "Name is required")
    String name
) {
}

package site.secmega.secapi.feature.time.dto;

import jakarta.validation.constraints.NotBlank;

public record TimeRequest(
        @NotBlank(message = "Time is required")
        String name
) {
}

package site.secmega.secapi.feature.size.dto;

import jakarta.validation.constraints.NotNull;

public record SizeRequest(
        @NotNull(message = "Size cannot be null")
        String size
) {
}

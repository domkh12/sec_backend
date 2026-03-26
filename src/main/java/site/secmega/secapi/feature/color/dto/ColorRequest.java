package site.secmega.secapi.feature.color.dto;

import jakarta.validation.constraints.NotNull;

public record ColorRequest(
        @NotNull(message = "Color cannot be null")
        String color
) {
}

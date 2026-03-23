package site.secmega.secapi.feature.buyer.dto;

import jakarta.validation.constraints.NotNull;

public record BuyerRequest(
        @NotNull(message = "Name cannot be null")
        String name
) {
}

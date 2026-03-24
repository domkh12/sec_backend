package site.secmega.secapi.feature.buyer.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record BuyerRequest(
        @NotNull(message = "Name cannot be null")
        String name,
        List<String> files
) {
}

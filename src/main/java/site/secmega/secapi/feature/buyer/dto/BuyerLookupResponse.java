package site.secmega.secapi.feature.buyer.dto;

import lombok.Builder;

@Builder
public record BuyerLookupResponse(
        Long id,
        String name
) {
}

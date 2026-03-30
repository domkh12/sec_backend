package site.secmega.secapi.feature.product.dto;

import lombok.Builder;

@Builder
public record ProductStateResponse(
        long totalStyleNo,
        long totalActive,
        long totalDraft
) {
}

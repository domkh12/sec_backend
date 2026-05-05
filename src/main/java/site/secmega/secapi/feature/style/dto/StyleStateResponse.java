package site.secmega.secapi.feature.style.dto;

import lombok.Builder;

@Builder
public record StyleStateResponse(
        long totalStyleNo,
        long totalActive,
        long totalDraft
) {
}

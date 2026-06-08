package site.secmega.secapi.feature.analysis.dto;

import lombok.Builder;

@Builder
public record BuyerAnalysisResponse(
        Long id,
        String name,
        Integer mos,
        Integer outputQty,
        Integer inputQty
) {
}

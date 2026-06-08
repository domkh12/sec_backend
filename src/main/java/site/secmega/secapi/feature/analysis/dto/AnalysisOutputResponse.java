package site.secmega.secapi.feature.analysis.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record AnalysisOutputResponse(
    Integer totalOutput,
    Integer totalInput,
    Integer totalStyleActive,
    Integer totalActiveStyle,
    Integer totalBalance,
    List<MoResponse> mo,
    List<BuyerAnalysisResponse> buyers
) {
}

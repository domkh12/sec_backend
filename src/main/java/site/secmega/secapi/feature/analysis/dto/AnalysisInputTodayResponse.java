package site.secmega.secapi.feature.analysis.dto;

import lombok.Builder;

@Builder
public record AnalysisInputTodayResponse(
        Integer totalJob,
        Integer totalCutting,
        Integer activeStyle,
        Integer balanceCutting
) {
}

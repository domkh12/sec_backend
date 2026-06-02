package site.secmega.secapi.feature.analysis.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record AnalysisOutputResponse(
    Integer totalOutput,
    Integer totalInput,
    Integer totalActiveStyle,
    List<MoResponse> mo
) {
}

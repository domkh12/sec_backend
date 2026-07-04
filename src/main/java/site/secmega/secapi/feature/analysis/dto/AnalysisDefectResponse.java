package site.secmega.secapi.feature.analysis.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record AnalysisDefectResponse(
        String updatedAt,
        Double targetDefectRate,
        List<LineDefectResponse> lines
) {
}

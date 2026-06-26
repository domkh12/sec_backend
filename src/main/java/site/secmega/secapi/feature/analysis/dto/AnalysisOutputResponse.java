package site.secmega.secapi.feature.analysis.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record AnalysisOutputResponse(
        Integer totalInput,
        Integer totalOutput,
        Double defectRate,
        Double efficiency,
        ComparisonResponse totalInputComparison,
        ComparisonResponse totalOutputComparison,
        List<LineChartDataResponse> data
) {
}

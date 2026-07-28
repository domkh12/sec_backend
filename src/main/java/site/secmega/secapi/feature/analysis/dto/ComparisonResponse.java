package site.secmega.secapi.feature.analysis.dto;

import lombok.Builder;

@Builder
public record ComparisonResponse(
        int current,
        int previous,
        int diff,
        double changePercent,
        String trend
) {
}

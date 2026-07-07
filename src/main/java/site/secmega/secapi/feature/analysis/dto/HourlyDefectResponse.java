package site.secmega.secapi.feature.analysis.dto;

import lombok.Builder;

@Builder
public record HourlyDefectResponse(
        String hour,
        Integer output,
        Integer defect
) {
}

package site.secmega.secapi.feature.analysis.dto;

import lombok.Builder;

import java.time.LocalDate;

@Builder
public record LineChartDataResponse(
        LocalDate date,
        Integer input,
        Integer output
) {
}

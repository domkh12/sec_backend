package site.secmega.secapi.feature.analysis.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record LineDataResponse(
        String x,
        Integer y,
        String buyer,
        List<MoResponse> mos
) {
}

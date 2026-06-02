package site.secmega.secapi.feature.analysis.dto;

import lombok.Builder;

@Builder
public record MoResponse(
        String mo,
        Integer outputQty
) {
}

package site.secmega.secapi.feature.analysis.dto;

import lombok.Builder;

@Builder
public record SizeOutput(
        String size,
        Integer inputQty,
        Integer outputQty
) {
}

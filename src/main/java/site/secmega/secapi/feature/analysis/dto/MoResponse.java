package site.secmega.secapi.feature.analysis.dto;

import lombok.Builder;
import site.secmega.secapi.feature.color.dto.ColorLookupResponse;

import java.util.List;

@Builder
public record MoResponse(
        String mo,
        Integer outputQty,
        String buyer,
        Integer inputQty,
        List<SizeOutput> sizeOutputs,
        ColorLookupResponse color
) {
}

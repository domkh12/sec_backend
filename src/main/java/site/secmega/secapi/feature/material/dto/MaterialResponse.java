package site.secmega.secapi.feature.material.dto;

import lombok.Builder;
import site.secmega.secapi.base.MaterialStatus;
import site.secmega.secapi.feature.color.dto.ColorLookupResponse;
import site.secmega.secapi.feature.size.dto.SizeLookupResponse;
import site.secmega.secapi.feature.style.dto.StyleLookupResponse;

import java.util.List;

@Builder
public record MaterialResponse(
        Long id,
        String code,
        String name,
        String description,
        Double balance,
        String unit,
        String image,
        Double totalInput,
        Double totalOutput,
        MaterialStatus status,
        ColorLookupResponse color,
        SizeLookupResponse size,
        List<StyleLookupResponse> styles
) {
}

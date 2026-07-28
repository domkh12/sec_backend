package site.secmega.secapi.feature.defectType.dto;

import lombok.Builder;

@Builder
public record DefectTypeWithQtyResponse(
        Long id,
        String type,
        Integer qty
) {
}

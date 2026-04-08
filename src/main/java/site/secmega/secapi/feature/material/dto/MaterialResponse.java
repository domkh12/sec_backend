package site.secmega.secapi.feature.material.dto;

import lombok.Builder;
import site.secmega.secapi.base.MaterialStatus;

@Builder
public record MaterialResponse(
        Long id,
        String code,
        String name,
        Double balance,
        String unit,
        String image,
        Double totalInput,
        Double totalOutput,
        MaterialStatus status
) {
}

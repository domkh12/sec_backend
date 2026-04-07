package site.secmega.secapi.feature.material.dto;

import site.secmega.secapi.base.MaterialStatus;

public record MaterialResponse(
        Long id,
        String code,
        String name,
        Double balance,
        String unit,
        String image,
        MaterialStatus status
) {
}

package site.secmega.secapi.feature.material.dto;

import lombok.Builder;

@Builder
public record MaterialStatResponse(
        long totalMaterial,
        long totalLowStock,
        long totalOutOfStock
) {
}

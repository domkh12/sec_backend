package site.secmega.secapi.feature.material.dto;

import lombok.Builder;

@Builder
public record MaterialReportResponse(
         String code,
         String name,
         String description,
         String styles,
         String unit,
         String size,
         String color,
         Double balance
) {
}

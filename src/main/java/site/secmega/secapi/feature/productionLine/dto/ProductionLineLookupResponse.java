package site.secmega.secapi.feature.productionLine.dto;

import lombok.Builder;

@Builder
public record ProductionLineLookupResponse(
    Long id,
    String name
) {
}

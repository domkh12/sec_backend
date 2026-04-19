package site.secmega.secapi.feature.processingTime.dto;

import lombok.Builder;

@Builder
public record ProcessOperationResponse(
        Long orderingNumber,
        String operationName,
        Long operationId
) {
}

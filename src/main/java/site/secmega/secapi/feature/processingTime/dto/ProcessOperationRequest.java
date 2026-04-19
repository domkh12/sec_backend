package site.secmega.secapi.feature.processingTime.dto;

import lombok.Builder;

@Builder
public record ProcessOperationRequest(
        Long orderingNumber,
        Long operationId
) {
}

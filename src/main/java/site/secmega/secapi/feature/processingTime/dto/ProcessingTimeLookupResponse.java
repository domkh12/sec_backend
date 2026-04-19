package site.secmega.secapi.feature.processingTime.dto;

import lombok.Builder;

@Builder
public record ProcessingTimeLookupResponse(
        Long id,
        String style
) {
}

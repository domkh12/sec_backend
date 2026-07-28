package site.secmega.secapi.feature.time.dto;

import lombok.Builder;

@Builder
public record TimeResponse(
        Long id,
        String name
) {
}

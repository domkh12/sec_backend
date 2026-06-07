package site.secmega.secapi.feature.color.dto;

import lombok.Builder;

@Builder
public record ColorLookupResponse (
        Long id,
        String color
) {
}

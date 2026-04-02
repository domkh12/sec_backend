package site.secmega.secapi.feature.size.dto;

import lombok.Builder;

@Builder
public record SizeLookupResponse(
        String size
) {
}

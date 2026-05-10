package site.secmega.secapi.feature.style.dto;

import lombok.Builder;

@Builder
public record StyleLookupResponse(
        Long id,
        String styleNo
) {
}

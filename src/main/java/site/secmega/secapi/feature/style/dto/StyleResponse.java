package site.secmega.secapi.feature.style.dto;

import lombok.Builder;
import site.secmega.secapi.base.StyleStatus;

@Builder
public record StyleResponse(
    Long id,
    String styleNo,
    String description,
    StyleStatus status
) {
    
}

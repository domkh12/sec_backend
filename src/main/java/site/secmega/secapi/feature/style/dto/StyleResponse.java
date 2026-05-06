package site.secmega.secapi.feature.style.dto;

import site.secmega.secapi.base.StyleStatus;

public record StyleResponse(
    Long id,
    String styleNo,
    String description,
    StyleStatus status
) {
    
}

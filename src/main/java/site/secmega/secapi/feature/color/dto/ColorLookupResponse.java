package site.secmega.secapi.feature.color.dto;

import lombok.Builder;
import site.secmega.secapi.feature.size.dto.SizeLookupResponse;

import java.util.List;

@Builder
public record ColorLookupResponse (
        Long id,
        String color
) {
}

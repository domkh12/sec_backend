package site.secmega.secapi.feature.file.dto;

import lombok.Builder;

@Builder
public record FileResponse(
        String name,
        Long size,
        String extension,
        String uri
) {
}

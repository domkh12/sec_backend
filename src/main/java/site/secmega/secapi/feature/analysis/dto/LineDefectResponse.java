package site.secmega.secapi.feature.analysis.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record LineDefectResponse(
        String line,
        List<MosResponse> mos
) {
}

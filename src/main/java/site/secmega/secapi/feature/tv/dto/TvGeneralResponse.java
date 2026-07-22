package site.secmega.secapi.feature.tv.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record TvGeneralResponse(
        String line,
        Integer worker,
        Integer helper,
        List<TvGeneralStyleResponse> styles
) {
}

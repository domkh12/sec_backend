package site.secmega.secapi.feature.tv.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record TvDataResponse(
        Long id,
        String line,
        Integer worker,
        Integer helper,
        List<TvOrderResponse> orders
) {
}

package site.secmega.secapi.feature.tv.dto;

import lombok.Builder;

@Builder
public record TvGeneralStyleResponse(
        String orderNo,
        String sewStart,
        Integer day,
        Integer wHour,
        Integer tarH,
        Integer tarDay,
        Integer yFinish,
        Integer defects,
        Integer h8,
        Integer h9,
        Integer h10,
        Integer h11,
        Integer h13,
        Integer h14,
        Integer h15,
        Integer h16,
        Integer h17,
        Integer h18
) {
}

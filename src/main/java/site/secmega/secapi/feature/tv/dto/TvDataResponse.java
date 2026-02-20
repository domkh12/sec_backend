package site.secmega.secapi.feature.tv.dto;

import lombok.Builder;

@Builder
public record TvDataResponse(
        String line,
        Integer worker,
        String orderNo,
        Integer orderQty,
        Integer totalInLine,
        Integer totalOutput,
        Integer orderInline,
        Integer balanceInLine,
        Integer qcRepairBack,
        Integer balanceDay
) {
}

package site.secmega.secapi.feature.buyer.dto;

import lombok.Builder;

@Builder
public record BuyerStatsResponse(
        Integer totalBuyer,
        Integer activeOrder,
        Integer totalPcs
) {
}

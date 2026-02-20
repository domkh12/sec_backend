package site.secmega.secapi.feature.tv.dto;

public record TvDataRequest(
        String line,
        Integer worker,
        String orderNo,
        Integer orderQty,
        Integer totalInLine,
        Integer totalOutput,
        Integer orderInline,
        Integer balanceInLine,
        Integer qcRepairBack,
        Integer balanceDay,
        String tvName
) {
}

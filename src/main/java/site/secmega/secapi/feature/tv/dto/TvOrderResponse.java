package site.secmega.secapi.feature.tv.dto;

import java.util.List;

public record TvOrderResponse(
        Long id,
        String orderNo,
        String status,
        Integer orderQty,
        Integer totalInLine,
        Integer totalOutput,
        Integer orderInline,
        Integer balanceInLine,
        Integer qcRepairBack,
        Integer balanceDay,
        Integer input,
        Integer wHour,
        Integer hTarg,
        String startDate,
        String finishDate,
        List<DailyRecord> dailyRecords,
        Defect defects
) {
}

package site.secmega.secapi.feature.tv.dto;

import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Builder
public record TvDataResponse(
        String line,
        Integer worker,
        Long day,
        String orderNo,
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
        Integer dTarg,
        LocalDate startDate,
        LocalDate finishDate,
        List<DailyRecord> dailyRecords,
        Defect defects
) {
}

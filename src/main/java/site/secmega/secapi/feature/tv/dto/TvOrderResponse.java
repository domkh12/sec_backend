package site.secmega.secapi.feature.tv.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;

@Builder
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
        @JsonFormat(pattern = "dd/MMM/yyyy")
        LocalDate startDate,
        @JsonFormat(pattern = "dd/MMM/yyyy")
        LocalDate finishDate,
        List<DailyRecord> dailyRecords,
        Defect defects
) {
}

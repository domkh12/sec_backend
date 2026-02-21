package site.secmega.secapi.feature.tv.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

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
        String tvName,
        Integer hTarg,
        Integer wHour,
        Integer input,
        Integer dTarg,
        LocalDate startDate,
        LocalDate finishDate,
        Integer h8,
        Integer h9,
        Integer h10,
        Integer h11,
        Integer h13,
        Integer h14,
        Integer h15,
        Integer h16,
        Integer h17,
        Integer h18,
        Integer dh8,
        Integer dh9,
        Integer dh10,
        Integer dh11,
        Integer dh13,
        Integer dh14,
        Integer dh15,
        Integer dh16,
        Integer dh17,
        Integer dh18
) {
}

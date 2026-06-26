package site.secmega.secapi.feature.outputDetail.dto;

import lombok.Builder;
import site.secmega.secapi.feature.productionLine.dto.ProductionLineLookupResponse;
import site.secmega.secapi.feature.size.dto.SizeLookupResponse;
import site.secmega.secapi.feature.time.dto.TimeResponse;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
public record OutputDetailResponse(
    Long id,
    LocalDateTime reportDate,
    String mo,
    SizeLookupResponse size,
    Integer qty,
    LocalDate outputDate,
    TimeResponse time,
    ProductionLineLookupResponse line
) {
}

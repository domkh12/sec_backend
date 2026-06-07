package site.secmega.secapi.feature.defectDetail.dto;

import java.time.LocalDate;

public record DefectDetailRequest(
        Long lineId,
        Long timeId,
        Long defectTypeId,
        Long woId,
        LocalDate defectDate,
        Integer defectQty
) {
}

package site.secmega.secapi.feature.defectDetail.dto;

import java.time.LocalDate;

public record DefectDetailFilterRequest(
        Integer pageNo,
        Integer pageSize,
        String search,
        Long lineId,
        Long buyerId,
        LocalDate reportDate
) {
    public DefectDetailFilterRequest{
        if (pageNo == null){
            pageNo = 1;
        }
        if (pageSize == null){
            pageSize = 20;
        }
    }
}

package site.secmega.secapi.feature.outputDetail.dto;

import java.time.LocalDate;

public record OutputFilterRequest(
        Integer pageNo,
        Integer pageSize,
        String search,
        Long lineId,
        Long sizeId,
        Long buyerId,
        LocalDate reportDate
) {
    public OutputFilterRequest{
        if (pageNo == null){
            pageNo = 1;
        }
        if (pageSize == null){
            pageSize = 20;
        }
    }
}

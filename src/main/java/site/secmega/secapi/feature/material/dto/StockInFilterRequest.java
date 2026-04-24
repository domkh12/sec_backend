package site.secmega.secapi.feature.material.dto;

import site.secmega.secapi.base.MaterialStatus;

public record StockInFilterRequest(
        Integer pageNo,
        Integer pageSize,
        String search
) {
    public StockInFilterRequest{
        if (pageNo == null){
            pageNo = 1;
        }
        if (pageSize == null){
            pageSize = 20;
        }
    }
}

package site.secmega.secapi.feature.material.dto;

import site.secmega.secapi.base.MaterialStatus;

public record MaterialFilterRequest(
        Integer pageNo,
        Integer pageSize,
        String search,
        MaterialStatus status,
        String unit,
        String size,
        String color
) {
    public MaterialFilterRequest{
        if (pageNo == null){
            pageNo = 1;
        }
        if (pageSize == null){
            pageSize = 20;
        }
    }
}

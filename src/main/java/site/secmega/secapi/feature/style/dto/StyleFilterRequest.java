package site.secmega.secapi.feature.style.dto;

import site.secmega.secapi.base.StyleStatus;

public record StyleFilterRequest(
        Integer pageNo,
        Integer pageSize,
        String search,
        Long subCategoryId,
        Long colorId,
        Long sizeId,
        StyleStatus status
) {
    public StyleFilterRequest {
        if (pageNo == null){
            pageNo = 1;
        }
        if (pageSize == null){
            pageSize = 20;
        }
    }
}

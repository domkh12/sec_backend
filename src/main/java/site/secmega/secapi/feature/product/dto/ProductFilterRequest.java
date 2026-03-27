package site.secmega.secapi.feature.product.dto;

import java.util.List;

public record ProductFilterRequest(
        Integer pageNo,
        Integer pageSize,
        String search,
        Long subCategoryId,
        Long colorId,
        List<Long> sizeId
) {
    public ProductFilterRequest{
        if (pageNo == null){
            pageNo = 1;
        }
        if (pageSize == null){
            pageSize = 20;
        }
    }
}

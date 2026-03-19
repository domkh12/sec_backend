package site.secmega.secapi.feature.subCategory.dto;

public record SubCategoryFilterRequest(
        Integer pageNo,
        Integer pageSize
) {
    public SubCategoryFilterRequest{
        if (pageNo == null || pageNo < 1){
            pageNo = 1;
        }
        if (pageSize == null || pageSize < 1){
            pageSize = 20;
        }
    }
}

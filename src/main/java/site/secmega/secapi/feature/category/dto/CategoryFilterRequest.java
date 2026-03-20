package site.secmega.secapi.feature.category.dto;

public record CategoryFilterRequest(
        Integer pageNo,
        Integer pageSize,
        String search
) {
    public CategoryFilterRequest{
        if (pageNo == null){
            pageNo = 1;
        }
        if (pageSize == null){
            pageSize = 20;
        }
    }
}

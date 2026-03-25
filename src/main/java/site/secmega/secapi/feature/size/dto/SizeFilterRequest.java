package site.secmega.secapi.feature.size.dto;

public record SizeFilterRequest(
        Integer pageNo,
        Integer pageSize,
        String search
) {
    public SizeFilterRequest{
        if (pageNo == null){
            pageNo = 1;
        }
        if (pageSize == null){
            pageSize = 20;
        }
    }
}

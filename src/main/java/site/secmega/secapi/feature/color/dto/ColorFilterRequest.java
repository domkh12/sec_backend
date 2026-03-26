package site.secmega.secapi.feature.color.dto;

public record ColorFilterRequest(
        Integer pageNo,
        Integer pageSize,
        String search
) {
    public ColorFilterRequest{
        if (pageNo == null){
            pageNo = 1;
        }
        if (pageSize == null){
            pageSize = 20;
        }
    }
}

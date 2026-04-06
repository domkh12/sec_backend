package site.secmega.secapi.feature.material.dto;

public record MaterialFilterRequest(
        Integer pageNo,
        Integer pageSize,
        String search
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

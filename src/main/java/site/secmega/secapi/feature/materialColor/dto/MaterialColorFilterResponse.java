package site.secmega.secapi.feature.materialColor.dto;

public record MaterialColorFilterResponse(
        Integer pageNo,
        Integer pageSize,
        String search
) {
    public MaterialColorFilterResponse{
        if (pageNo == null){
            pageNo = 1;
        }
        if (pageSize == null){
            pageSize = 20;
        }
    }
}

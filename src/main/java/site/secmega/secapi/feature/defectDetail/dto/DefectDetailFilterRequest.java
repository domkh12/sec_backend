package site.secmega.secapi.feature.defectDetail.dto;

public record DefectDetailFilterRequest(
        Integer pageNo,
        Integer pageSize,
        String search
) {
    public DefectDetailFilterRequest{
        if (pageNo == null){
            pageNo = 1;
        }
        if (pageSize == null){
            pageSize = 20;
        }
    }
}

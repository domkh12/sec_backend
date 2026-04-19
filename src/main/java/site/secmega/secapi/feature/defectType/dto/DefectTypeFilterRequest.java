package site.secmega.secapi.feature.defectType.dto;

public record DefectTypeFilterRequest(
        Integer pageNo,
        Integer pageSize,
        String search
) {
    public DefectTypeFilterRequest {
        if (pageNo == null){
            pageNo = 1;
        }
        if (pageSize == null){
            pageSize = 20;
        }
    }
}

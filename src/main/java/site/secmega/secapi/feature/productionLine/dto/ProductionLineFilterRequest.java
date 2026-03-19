package site.secmega.secapi.feature.productionLine.dto;

public record ProductionLineFilterRequest(
        Integer pageNo,
        Integer pageSize,
        String search,
        Long departmentId
) {
    public ProductionLineFilterRequest{
        if (pageNo == null){
            pageNo = 1;
        }
        if (pageSize == null){
            pageSize = 20;
        }
    }
}

package site.secmega.secapi.feature.unit.dto;

public record UnitFilterRequest(
        Integer pageNo,
        Integer pageSize,
        String search
) {
    public UnitFilterRequest{
        if (pageNo == null){
            pageNo = 1;
        }
        if (pageSize == null){
            pageSize = 20;
        }
    }
}

package site.secmega.secapi.feature.material.dto;

public record StockInFilterRequest(
        Integer pageNo,
        Integer pageSize,
        String search
) {
    public StockInFilterRequest{
        if (pageNo == null){
            pageNo = 1;
        }
        if (pageSize == null){
            pageSize = 20;
        }
    }
}

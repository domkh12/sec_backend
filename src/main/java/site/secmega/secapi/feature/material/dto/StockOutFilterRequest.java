package site.secmega.secapi.feature.material.dto;


public record StockOutFilterRequest(
        Integer pageNo,
        Integer pageSize,
        String search

) {
    public StockOutFilterRequest{
        if (pageNo == null){
            pageNo = 1;
        }
        if (pageSize == null){
            pageSize = 20;
        }
    }
}

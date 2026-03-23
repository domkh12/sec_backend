package site.secmega.secapi.feature.buyer.dto;

public record BuyerFilterRequest(
        Integer pageNo,
        Integer pageSize,
        String search
) {
    public BuyerFilterRequest{
        if (pageNo == null){
            pageNo = 1;
        }
        if (pageSize == null){
            pageSize = 20;
        }
    }
}

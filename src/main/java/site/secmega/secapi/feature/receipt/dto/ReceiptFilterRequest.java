package site.secmega.secapi.feature.receipt.dto;

public record ReceiptFilterRequest(
        Integer pageNo,
        Integer pageSize,
        String search
) {
    public ReceiptFilterRequest{
        if (pageNo == null){
            pageNo = 1;
        }
        if (pageSize == null){
            pageSize = 20;
        }
    }
}

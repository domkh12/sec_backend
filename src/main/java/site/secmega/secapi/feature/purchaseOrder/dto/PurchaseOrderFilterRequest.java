package site.secmega.secapi.feature.purchaseOrder.dto;

public record PurchaseOrderFilterRequest(
        Integer pageNo,
        Integer pageSize,
        String search
) {
    public PurchaseOrderFilterRequest{
        if (pageNo == null){
            pageNo = 1;
        }
        if (pageSize == null){
            pageSize = 20;
        }
    }
}

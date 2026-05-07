package site.secmega.secapi.feature.purchaseOrder.dto;

import site.secmega.secapi.base.POStatus;

public record PurchaseOrderFilterRequest(
        Integer pageNo,
        Integer pageSize,
        String search,
        POStatus status,
        Long styleId,
        Long buyerId
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

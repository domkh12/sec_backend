package site.secmega.secapi.mapper;

import org.mapstruct.Mapper;
import site.secmega.secapi.domain.PurchaseOrder;
import site.secmega.secapi.feature.purchaseOrder.dto.PurchaseOrderRequest;
import site.secmega.secapi.feature.purchaseOrder.dto.PurchaseOrderResponse;

@Mapper(componentModel = "spring")
public interface PurchaseOrderMapper {
    PurchaseOrder fromPurchaseOrderRequest(PurchaseOrderRequest purchaseOrderRequest);
    PurchaseOrderResponse toPurchaseOrderResponse(PurchaseOrder purchaseOrder);
}

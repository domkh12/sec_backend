package site.secmega.secapi.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import site.secmega.secapi.domain.PurchaseOrder;
import site.secmega.secapi.feature.purchaseOrder.dto.PurchaseOrderLookupResponse;
import site.secmega.secapi.feature.purchaseOrder.dto.PurchaseOrderRequest;
import site.secmega.secapi.feature.purchaseOrder.dto.PurchaseOrderResponse;

@Mapper(componentModel = "spring")
public interface PurchaseOrderMapper {
    PurchaseOrder fromPurchaseOrderRequest(PurchaseOrderRequest purchaseOrderRequest);
    PurchaseOrderResponse toPurchaseOrderResponse(PurchaseOrder purchaseOrder);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromPurchaseOrderRequest(PurchaseOrderRequest purchaseOrderRequest,@MappingTarget PurchaseOrder purchaseOrder);
    PurchaseOrderLookupResponse toPurchaseOrderLookupResponse(PurchaseOrder purchaseOrder);
}

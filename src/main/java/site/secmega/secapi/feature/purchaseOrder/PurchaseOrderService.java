package site.secmega.secapi.feature.purchaseOrder;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import site.secmega.secapi.feature.purchaseOrder.dto.PurchaseOrderFilterRequest;
import site.secmega.secapi.feature.purchaseOrder.dto.PurchaseOrderRequest;
import site.secmega.secapi.feature.purchaseOrder.dto.PurchaseOrderResponse;

public interface PurchaseOrderService {
    void deletePO(Long id);

    PurchaseOrderResponse createPO(@Valid PurchaseOrderRequest purchaseOrderRequest);

    Page<PurchaseOrderResponse> getPO(PurchaseOrderFilterRequest purchaseOrderFilterRequest);

    PurchaseOrderResponse updatePO(Long id, @Valid PurchaseOrderRequest purchaseOrderRequest);
}

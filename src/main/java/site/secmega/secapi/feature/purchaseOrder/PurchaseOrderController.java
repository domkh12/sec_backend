package site.secmega.secapi.feature.purchaseOrder;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.secmega.secapi.feature.purchaseOrder.dto.PurchaseOrderFilterRequest;
import site.secmega.secapi.feature.purchaseOrder.dto.PurchaseOrderRequest;
import site.secmega.secapi.feature.purchaseOrder.dto.PurchaseOrderResponse;

@RestController
@RequestMapping("/api/v1/purchase-orders")
@RequiredArgsConstructor
public class PurchaseOrderController {

    private final PurchaseOrderService purchaseOrderService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deletePO(@PathVariable Long id){
        purchaseOrderService.deletePO(id);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    PurchaseOrderResponse updatePO(@PathVariable Long id, @RequestBody @Valid PurchaseOrderRequest purchaseOrderRequest){
        return purchaseOrderService.updatePO(id, purchaseOrderRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<PurchaseOrderResponse> getPO(@ModelAttribute PurchaseOrderFilterRequest purchaseOrderFilterRequest){
        return purchaseOrderService.getPO(purchaseOrderFilterRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    PurchaseOrderResponse createPO(@RequestBody @Valid PurchaseOrderRequest purchaseOrderRequest){
        return purchaseOrderService.createPO(purchaseOrderRequest);
    }

}

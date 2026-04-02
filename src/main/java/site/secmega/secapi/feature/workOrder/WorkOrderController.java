package site.secmega.secapi.feature.workOrder;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.secmega.secapi.feature.workOrder.dto.WorkOrderFilterRequest;
import site.secmega.secapi.feature.workOrder.dto.WorkOrderRequest;
import site.secmega.secapi.feature.workOrder.dto.WorkOrderResponse;

@RestController
@RequestMapping("/api/v1/work-orders")
@RequiredArgsConstructor
public class WorkOrderController {

    private final WorkOrderService workOrderService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    WorkOrderResponse createWorkOrder(@RequestBody @Valid WorkOrderRequest workOrderRequest){
        return workOrderService.createWorkOrder(workOrderRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<WorkOrderResponse> findAll(@ModelAttribute WorkOrderFilterRequest workOrderFilterRequest){
        return workOrderService.findAll(workOrderFilterRequest);
    }


}

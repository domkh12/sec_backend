package site.secmega.secapi.feature.workOrder;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.secmega.secapi.feature.workOrder.dto.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/work-orders")
@RequiredArgsConstructor
public class  WorkOrderController {

    private final WorkOrderService workOrderService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    WorkOrderStatResponse getWorkOrderStat(@ModelAttribute WorkOrderFilterRequest workOrderFilterRequest){
        return workOrderService.getWorkOrderStat(workOrderFilterRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("/style/{mo}")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<Map<String, String>> getStyleByMo(@PathVariable String mo){
        return workOrderService.getStyleByMo(mo);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteWorkOrder(@PathVariable Long id){
        workOrderService.deleteWorkOrder(id);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("/lookup")
    @ResponseStatus(HttpStatus.OK)
    List<WorkOrderLookupResponse> getWorkOrderLookup(){
        return workOrderService.getWorkOrderLookup();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    WorkOrderResponse updateWorkOrder(@PathVariable Long id, @RequestBody WorkOrderRequest workOrderRequest){
        return workOrderService.updateWorkOrder(id, workOrderRequest);
    }

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

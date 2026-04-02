package site.secmega.secapi.feature.workOrder;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import site.secmega.secapi.feature.workOrder.dto.WorkOrderFilterRequest;
import site.secmega.secapi.feature.workOrder.dto.WorkOrderRequest;
import site.secmega.secapi.feature.workOrder.dto.WorkOrderResponse;

public interface WorkOrderService {
    Page<WorkOrderResponse> findAll(WorkOrderFilterRequest workOrderFilterRequest);

    WorkOrderResponse createWorkOrder(@Valid WorkOrderRequest workOrderRequest);
}

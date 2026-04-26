package site.secmega.secapi.feature.workOrder;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import site.secmega.secapi.feature.workOrder.dto.*;

import java.util.List;
import java.util.Map;

public interface WorkOrderService {
    void deleteWorkOrder(Long id);

    WorkOrderResponse updateWorkOrder(Long id, WorkOrderRequest workOrderRequest);

    WorkOrderStatResponse getWorkOrderStat(WorkOrderFilterRequest workOrderFilterRequest);

    Page<WorkOrderResponse> findAll(WorkOrderFilterRequest workOrderFilterRequest);

    WorkOrderResponse createWorkOrder(@Valid WorkOrderRequest workOrderRequest);

    List<WorkOrderLookupResponse> getWorkOrderLookup();

    ResponseEntity<Map<String, String>> getStyleByMo(String mo);
}

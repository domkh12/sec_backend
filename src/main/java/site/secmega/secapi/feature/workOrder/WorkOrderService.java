package site.secmega.secapi.feature.workOrder;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import site.secmega.secapi.feature.workOrder.dto.WorkOrderFilterRequest;
import site.secmega.secapi.feature.workOrder.dto.WorkOrderLookupResponse;
import site.secmega.secapi.feature.workOrder.dto.WorkOrderRequest;
import site.secmega.secapi.feature.workOrder.dto.WorkOrderResponse;

import java.util.List;
import java.util.Map;

public interface WorkOrderService {
    Page<WorkOrderResponse> findAll(WorkOrderFilterRequest workOrderFilterRequest);

    WorkOrderResponse createWorkOrder(@Valid WorkOrderRequest workOrderRequest);

    List<WorkOrderLookupResponse> getWorkOrderLookup();

    ResponseEntity<Map<String, String>> getStyleByMo(String mo);
}

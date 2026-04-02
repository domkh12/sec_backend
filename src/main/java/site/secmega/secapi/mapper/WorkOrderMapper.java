package site.secmega.secapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import site.secmega.secapi.domain.WorkOrder;
import site.secmega.secapi.feature.workOrder.dto.WorkOrderRequest;
import site.secmega.secapi.feature.workOrder.dto.WorkOrderResponse;

@Mapper(componentModel = "spring", uses = {ColorMapper.class, SizeMapper.class})
public interface WorkOrderMapper {
    @Mapping(target = "buyer", expression = "java(workOrder.getBuyer() != null ? workOrder.getBuyer().getName() : null)")
    WorkOrderResponse toWorkOrderResponse(WorkOrder workOrder);
    WorkOrder fromWorkOrderRequest(WorkOrderRequest workOrderRequest);
}

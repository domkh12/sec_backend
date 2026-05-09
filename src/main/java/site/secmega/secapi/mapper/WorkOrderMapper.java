package site.secmega.secapi.mapper;

import org.mapstruct.*;
import site.secmega.secapi.domain.WorkOrder;
import site.secmega.secapi.feature.workOrder.dto.WorkOrderRequest;
import site.secmega.secapi.feature.workOrder.dto.WorkOrderResponse;
import site.secmega.secapi.feature.workOrder.dto.WorkOrderStatusRequest;

@Mapper(componentModel = "spring", uses = {ColorMapper.class, SizeMapper.class})
public interface WorkOrderMapper {
    WorkOrderResponse toWorkOrderResponse(WorkOrder workOrder);
    WorkOrder fromWorkOrderRequest(WorkOrderRequest workOrderRequest);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateWorkOrder(WorkOrderRequest workOrderRequest,@MappingTarget WorkOrder workOrder);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateStatusWorkOrder(WorkOrderStatusRequest workOrderStatusRequest, @MappingTarget WorkOrder workOrder);
}

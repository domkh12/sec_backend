package site.secmega.secapi.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import site.secmega.secapi.domain.Warehouse;
import site.secmega.secapi.feature.warehouse.dto.WarehouseRequest;
import site.secmega.secapi.feature.warehouse.dto.WarehouseResponse;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {
    Warehouse formWarehouseRequest(WarehouseRequest warehouseRequest);
    WarehouseResponse toWarehouseResponse(Warehouse warehouse);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromWarehouseRequest(WarehouseRequest warehouseRequest, @MappingTarget Warehouse warehouse);
}

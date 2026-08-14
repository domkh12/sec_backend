package site.secmega.secapi.mapper;

import org.mapstruct.Mapper;
import site.secmega.secapi.domain.Warehouse;
import site.secmega.secapi.feature.warehouse.dto.WarehouseRequest;
import site.secmega.secapi.feature.warehouse.dto.WarehouseResponse;

@Mapper(componentModel = "spring")
public interface WarehouseMapper {
    Warehouse formWarehouseRequest(WarehouseRequest warehouseRequest);
    WarehouseResponse toWarehouseResponse(Warehouse warehouse);
}

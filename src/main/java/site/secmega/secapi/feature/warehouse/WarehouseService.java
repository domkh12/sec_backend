package site.secmega.secapi.feature.warehouse;

import org.springframework.data.domain.Page;
import site.secmega.secapi.feature.warehouse.dto.WarehouseFilterRequest;
import site.secmega.secapi.feature.warehouse.dto.WarehouseRequest;
import site.secmega.secapi.feature.warehouse.dto.WarehouseResponse;

public interface WarehouseService {
    Page<WarehouseResponse> findAll(WarehouseFilterRequest warehouseFilterRequest);

    WarehouseResponse createWarehouse(WarehouseRequest warehouseRequest);
}

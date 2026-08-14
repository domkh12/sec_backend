package site.secmega.secapi.feature.warehouse;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.secmega.secapi.feature.warehouse.dto.WarehouseFilterRequest;
import site.secmega.secapi.feature.warehouse.dto.WarehouseRequest;
import site.secmega.secapi.feature.warehouse.dto.WarehouseResponse;

@RestController
@RequestMapping("/api/v1/warehouses")
@RequiredArgsConstructor
public class WarehouseController {

    private final WarehouseService warehouseService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WAREHOUSE')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    WarehouseResponse createWarehouse(@RequestBody WarehouseRequest warehouseRequest){
        return warehouseService.createWarehouse(warehouseRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WAREHOUSE')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<WarehouseResponse> findAll(@ModelAttribute WarehouseFilterRequest warehouseFilterRequest){
        return warehouseService.findAll(warehouseFilterRequest);
    }


}

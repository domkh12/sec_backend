package site.secmega.secapi.feature.warehouse;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.domain.User;
import site.secmega.secapi.domain.Warehouse;
import site.secmega.secapi.feature.warehouse.dto.WarehouseFilterRequest;
import site.secmega.secapi.feature.warehouse.dto.WarehouseRequest;
import site.secmega.secapi.feature.warehouse.dto.WarehouseResponse;
import site.secmega.secapi.mapper.WarehouseMapper;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class WarehouseServiceImpl implements WarehouseService{

    private final WarehouseRepository warehouseRepository;
    private final WarehouseMapper warehouseMapper;

    @Override
    public void deleteWarehouse(String uuid) {
        Warehouse warehouse = warehouseRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Warehouse not found!")
        );
        warehouse.setDeletedAt(LocalDateTime.now());
        warehouseRepository.save(warehouse);
    }

    @Override
    public WarehouseResponse updateWarehouse(String uuid, WarehouseRequest warehouseRequest) {

        if (warehouseRepository.existsByNameAndUuidNot(warehouseRequest.name(), uuid)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Warehouse name already exist!");
        }

        if (warehouseRepository.existsByCodeAndUuidNot(warehouseRequest.code(), uuid)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Warehouse code already exist!");
        }

        Warehouse warehouse = warehouseRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Warehouse not found!")
        );
        warehouseMapper.updateFromWarehouseRequest(warehouseRequest, warehouse);
        Warehouse savedWarehouse = warehouseRepository.save(warehouse);

        return warehouseMapper.toWarehouseResponse(savedWarehouse);
    }

    @Override
    public WarehouseResponse createWarehouse(WarehouseRequest warehouseRequest) {

        if (warehouseRepository.existsByCode(warehouseRequest.code())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Warehouse code already exist!");
        }

        if (warehouseRepository.existsByName(warehouseRequest.name())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Warehouse name already exist!");
        }

        Warehouse warehouse = warehouseMapper.formWarehouseRequest(warehouseRequest);
        warehouse.setUuid(UUID.randomUUID().toString());
        Warehouse savedWarehouse = warehouseRepository.save(warehouse);
        return warehouseMapper.toWarehouseResponse(savedWarehouse);
    }

    @Override
    public Page<WarehouseResponse> findAll(WarehouseFilterRequest warehouseFilterRequest) {

        if (warehouseFilterRequest.pageNo() <= 0 || warehouseFilterRequest.pageSize() <= 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Warehouse pageNo or PageSize must be bigger than 0");
        }

        Specification<Warehouse> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (warehouseFilterRequest.search() != null){
            String searchTerm = "%" + warehouseFilterRequest.search().toLowerCase() + "%";
            spec = spec.and((root, query, cb) ->
                    cb.or(
                            cb.like(cb.lower(root.get("name")),    searchTerm),
                            cb.like(cb.lower(root.get("code")),    searchTerm),
                            cb.like(cb.lower(root.get("address")), searchTerm),
                            cb.like(cb.lower(root.get("city")),    searchTerm)
                    )
            );
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(warehouseFilterRequest.pageNo() - 1, warehouseFilterRequest.pageSize(), sort);
        Page<Warehouse> warehouses = warehouseRepository.findAll(spec, pageRequest);

        return warehouses.map(warehouseMapper::toWarehouseResponse);
    }

}

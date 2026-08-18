package site.secmega.secapi.feature.unit;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.domain.Unit;
import site.secmega.secapi.feature.unit.dto.UnitFilterRequest;
import site.secmega.secapi.feature.unit.dto.UnitRequest;
import site.secmega.secapi.feature.unit.dto.UnitResponse;
import site.secmega.secapi.mapper.UnitMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UnitServiceImpl implements UnitService{

    private final UnitRepository unitRepository;
    private final UnitMapper unitMapper;

    @Override
    public UnitResponse updateUnit(String uuid, UnitRequest unitRequest) {

        Unit unit = unitRepository.findByUuid(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Unit not found!")
        );
        if (unitRepository.existsByUnitCodeAndDeletedAtNull(unitRequest.unitCode())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unit code already exist!");
        }

        if (unitRepository.existsByUnitNameAndDeletedAtNull(unitRequest.unitName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unit name already exist!");
        }
        unitMapper.updateFromUnitRequest(unitRequest, unit);
        Unit savedUnit = unitRepository.save(unit);

        return unitMapper.toUnitResponse(savedUnit);
    }

    @Override
    public UnitResponse createUnit(UnitRequest unitRequest) {

        if (unitRepository.existsByUnitCodeAndDeletedAtNull(unitRequest.unitCode())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unit code already exist!");
        }

        if (unitRepository.existsByUnitNameAndDeletedAtNull(unitRequest.unitName())){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unit name already exist!");
        }

        Unit unit = unitMapper.fromUnitRequest(unitRequest);
        unit.setUuid(UUID.randomUUID().toString());
        Unit savedUnit = unitRepository.save(unit);

        return unitMapper.toUnitResponse(savedUnit);
    }

    @Override
    public Page<UnitResponse> findAll(UnitFilterRequest unitFilterRequest) {

        if (unitFilterRequest.pageNo() <= 0 || unitFilterRequest.pageSize() <= 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page no and Page size invalid!");
        }

        Specification<Unit> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (unitFilterRequest.search() != null){
            String searchTerm = "%" + unitFilterRequest.search().toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
               cb.like(cb.lower(root.get("unitCode")), searchTerm)
            ));
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(unitFilterRequest.pageNo() - 1, unitFilterRequest.pageSize(), sort);
        Page<Unit> units = unitRepository.findAll(spec, pageRequest);

        return units.map(unitMapper::toUnitResponse);
    }

}

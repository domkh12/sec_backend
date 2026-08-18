package site.secmega.secapi.feature.unit;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import site.secmega.secapi.feature.unit.dto.UnitFilterRequest;
import site.secmega.secapi.feature.unit.dto.UnitRequest;
import site.secmega.secapi.feature.unit.dto.UnitResponse;

public interface UnitService {
    UnitResponse updateUnit(String uuid, @Valid UnitRequest unitRequest);

    UnitResponse createUnit(@Valid UnitRequest unitRequest);

    Page<UnitResponse> findAll(UnitFilterRequest unitFilterRequest);
}

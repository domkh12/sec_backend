package site.secmega.secapi.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import site.secmega.secapi.domain.Unit;
import site.secmega.secapi.feature.unit.dto.UnitRequest;
import site.secmega.secapi.feature.unit.dto.UnitResponse;

@Mapper(componentModel = "spring")
public interface UnitMapper {

    UnitResponse toUnitResponse(Unit unit);
    Unit fromUnitRequest(UnitRequest unitRequest);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromUnitRequest(UnitRequest unitRequest,@MappingTarget Unit unit);
}

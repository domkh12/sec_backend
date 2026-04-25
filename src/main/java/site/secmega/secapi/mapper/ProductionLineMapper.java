package site.secmega.secapi.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import site.secmega.secapi.domain.ProductionLine;
import site.secmega.secapi.feature.productionLine.dto.ProductionLineLookupResponse;
import site.secmega.secapi.feature.productionLine.dto.ProductionLineRequest;
import site.secmega.secapi.feature.productionLine.dto.ProductionLineResponse;

@Mapper(componentModel = "spring")
public interface ProductionLineMapper {
    ProductionLineResponse toProductionLineResponse(ProductionLine productionLine);
    ProductionLine fromProductionLineRequest(ProductionLineRequest productionLineRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromProductionLineRequest(ProductionLineRequest productionLineRequest, @MappingTarget ProductionLine productionLine);

    ProductionLineLookupResponse toProductionLineLookupResponse(ProductionLine productionLine);
}

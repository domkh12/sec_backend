package site.secmega.secapi.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import site.secmega.secapi.domain.ProductionLine;
import site.secmega.secapi.feature.productionLine.dto.ProductionLineRequest;
import site.secmega.secapi.feature.productionLine.dto.ProductionLineResponse;

@Mapper(componentModel = "spring")
public interface ProductionLineMapper {
    ProductionLineResponse toProductionLineResponse(ProductionLine productionLine);
    ProductionLine fromProductionLineRequest(ProductionLineRequest productionLineRequest);
}

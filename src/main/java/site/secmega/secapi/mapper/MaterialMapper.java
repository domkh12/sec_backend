package site.secmega.secapi.mapper;

import org.mapstruct.*;
import site.secmega.secapi.domain.Material;
import site.secmega.secapi.domain.MaterialDetail;
import site.secmega.secapi.feature.material.dto.MaterialRequest;
import site.secmega.secapi.feature.material.dto.MaterialResponse;
import site.secmega.secapi.feature.material.dto.StockInResponse;

@Mapper(componentModel = "spring")
public interface MaterialMapper {
    Material fromMaterialRequest(MaterialRequest materialRequest);
    MaterialResponse toMaterialResponse(Material material);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromMaterialRequest(MaterialRequest materialRequest, @MappingTarget Material material);
}

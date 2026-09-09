package site.secmega.secapi.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import site.secmega.secapi.domain.MaterialColor;
import site.secmega.secapi.feature.materialColor.dto.MaterialColorRequest;
import site.secmega.secapi.feature.materialColor.dto.MaterialColorResponse;

@Mapper(componentModel = "spring")
public interface MaterialColorMapper {
    MaterialColor fromMaterialColorRequest(MaterialColorRequest materialColorRequest);
    MaterialColorResponse toMaterialColorResponse(MaterialColor materialColor);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromMaterialColorRequest(MaterialColorRequest materialColorRequest, @MappingTarget MaterialColor materialColor);
}

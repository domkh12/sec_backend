package site.secmega.secapi.mapper;

import org.mapstruct.Mapper;
import site.secmega.secapi.domain.MaterialColor;
import site.secmega.secapi.feature.materialColor.dto.MaterialColorRequest;
import site.secmega.secapi.feature.materialColor.dto.MaterialColorResponse;

@Mapper(componentModel = "spring")
public interface MaterialColorMapper {
    MaterialColor fromMaterialColorRequest(MaterialColorRequest materialColorRequest);
    MaterialColorResponse toMaterialColorResponse(MaterialColor materialColor);
}

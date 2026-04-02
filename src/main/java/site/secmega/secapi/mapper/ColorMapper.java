package site.secmega.secapi.mapper;

import org.mapstruct.*;
import site.secmega.secapi.domain.Color;
import site.secmega.secapi.feature.color.dto.ColorLookupResponse;
import site.secmega.secapi.feature.color.dto.ColorRequest;
import site.secmega.secapi.feature.color.dto.ColorResponse;

@Mapper(componentModel = "spring")
public interface ColorMapper {
    Color fromColorRequest(ColorRequest colorRequest);
    ColorResponse toColorResponse(Color color);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromColorRequest(ColorRequest colorRequest, @MappingTarget Color color);

    ColorLookupResponse toColorLookupResponse(Color color);
}

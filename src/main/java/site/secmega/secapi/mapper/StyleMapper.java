package site.secmega.secapi.mapper;

import org.mapstruct.*;
import site.secmega.secapi.domain.Style;
import site.secmega.secapi.feature.style.dto.StyleRequest;
import site.secmega.secapi.feature.style.dto.StyleResponse;

@Mapper(componentModel = "spring")
public interface StyleMapper {

    Style formStyleRequest(StyleRequest styleRequest);
    StyleResponse toStyleResponse(Style style);

}

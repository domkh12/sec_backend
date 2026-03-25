package site.secmega.secapi.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import site.secmega.secapi.domain.Size;
import site.secmega.secapi.feature.size.dto.SizeRequest;
import site.secmega.secapi.feature.size.dto.SizeResponse;

@Mapper(componentModel = "spring")
public interface SizeMapper {
    Size fromSizeRequest(SizeRequest sizeRequest);
    SizeResponse toSizeResponse(Size size);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromSizeRequest(SizeRequest sizeRequest, @MappingTarget Size size);

}

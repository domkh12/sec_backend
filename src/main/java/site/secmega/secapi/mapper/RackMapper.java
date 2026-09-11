package site.secmega.secapi.mapper;

import org.mapstruct.*;
import site.secmega.secapi.domain.Rack;
import site.secmega.secapi.feature.rack.dto.RackRequest;
import site.secmega.secapi.feature.rack.dto.RackResponse;

@Mapper(componentModel = "spring")
public interface RackMapper {
    @Mapping(target = "qrCodeImage", ignore = true)
    RackResponse toRackResponse(Rack rack);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRackRequest(RackRequest rackRequest,@MappingTarget Rack rack);
}

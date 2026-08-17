package site.secmega.secapi.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import site.secmega.secapi.domain.Rack;
import site.secmega.secapi.feature.rack.dto.RackRequest;
import site.secmega.secapi.feature.rack.dto.RackResponse;

@Mapper(componentModel = "spring")
public interface RackMapper {
    RackResponse toRackResponse(Rack rack);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromRackRequest(RackRequest rackRequest,@MappingTarget Rack rack);
}

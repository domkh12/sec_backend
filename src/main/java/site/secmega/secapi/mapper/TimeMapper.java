package site.secmega.secapi.mapper;

import org.mapstruct.Mapper;
import site.secmega.secapi.domain.Time;
import site.secmega.secapi.feature.time.dto.TimeLookupResponse;
import site.secmega.secapi.feature.time.dto.TimeRequest;
import site.secmega.secapi.feature.time.dto.TimeResponse;

@Mapper(componentModel = "spring")
public interface TimeMapper {
    TimeLookupResponse toTimeLookupResponse(Time time);
    Time fromTimeRequest(TimeRequest timeRequest);
    TimeResponse toTimeResponse(Time time);
}

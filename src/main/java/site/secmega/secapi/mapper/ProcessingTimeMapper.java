package site.secmega.secapi.mapper;

import org.mapstruct.Mapper;
import site.secmega.secapi.domain.ProcessingTime;
import site.secmega.secapi.feature.processingTime.dto.ProcessingTimeRequest;
import site.secmega.secapi.feature.processingTime.dto.ProcessingTimeResponse;

@Mapper(componentModel = "spring")
public interface ProcessingTimeMapper {
    ProcessingTimeResponse toProcessingTimeResponse(ProcessingTime processingTime);
    ProcessingTime fromProcessingTimeRequest(ProcessingTimeRequest processingTimeRequest);

}

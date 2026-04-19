package site.secmega.secapi.feature.processingTime;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import site.secmega.secapi.feature.processingTime.dto.ProcessingTimeFilterRequest;
import site.secmega.secapi.feature.processingTime.dto.ProcessingTimeLookupResponse;
import site.secmega.secapi.feature.processingTime.dto.ProcessingTimeRequest;
import site.secmega.secapi.feature.processingTime.dto.ProcessingTimeResponse;

import java.util.List;

public interface ProcessingTimeService {
    ProcessingTimeResponse createProcessingTime(@Valid ProcessingTimeRequest processingTimeRequest);

    Page<ProcessingTimeResponse> findAll(ProcessingTimeFilterRequest processingTimeFilterRequest);

    List<ProcessingTimeLookupResponse> findProcessingTimeLookup();
}

package site.secmega.secapi.feature.processingTime;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.secmega.secapi.feature.processingTime.dto.ProcessingTimeLookupResponse;
import site.secmega.secapi.feature.processingTime.dto.ProcessingTimeFilterRequest;
import site.secmega.secapi.feature.processingTime.dto.ProcessingTimeRequest;
import site.secmega.secapi.feature.processingTime.dto.ProcessingTimeResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/processing-times")
@RequiredArgsConstructor
public class ProcessingTimeController {

    private final ProcessingTimeService processingTimeService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/lookup")
    @ResponseStatus(HttpStatus.OK)
    List<ProcessingTimeLookupResponse> findProcessingTimeLookup(){
        return processingTimeService.findProcessingTimeLookup();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<ProcessingTimeResponse> findAll(@ModelAttribute ProcessingTimeFilterRequest processingTimeFilterRequest){
        return processingTimeService.findAll(processingTimeFilterRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ProcessingTimeResponse createProcessingTime(@RequestBody @Valid ProcessingTimeRequest processingTimeRequest){
        return processingTimeService.createProcessingTime(processingTimeRequest);
    }

}

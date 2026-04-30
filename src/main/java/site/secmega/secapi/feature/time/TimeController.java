package site.secmega.secapi.feature.time;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.secmega.secapi.feature.time.dto.TimeLookupResponse;
import site.secmega.secapi.feature.time.dto.TimeRequest;
import site.secmega.secapi.feature.time.dto.TimeResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/times")
@RequiredArgsConstructor
public class TimeController {

    private final TimeService timeService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_OPERATOR')")
    @PostMapping()
    @ResponseStatus(HttpStatus.OK)
    TimeResponse createTime(@RequestBody TimeRequest timeRequest){
        return timeService.createTime(timeRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_OPERATOR')")
    @GetMapping("/lookup")
    @ResponseStatus(HttpStatus.OK)
    List<TimeLookupResponse> findTimeLookup(){
        return timeService.findTimeLookup();
    }
}

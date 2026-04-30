package site.secmega.secapi.feature.time;

import site.secmega.secapi.feature.time.dto.TimeLookupResponse;
import site.secmega.secapi.feature.time.dto.TimeRequest;
import site.secmega.secapi.feature.time.dto.TimeResponse;

import java.util.List;

public interface TimeService {
    TimeResponse createTime(TimeRequest timeRequest);

    List<TimeLookupResponse> findTimeLookup();
}

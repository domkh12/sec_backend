package site.secmega.secapi.feature.time;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.secmega.secapi.domain.Time;
import site.secmega.secapi.feature.time.dto.TimeLookupResponse;
import site.secmega.secapi.feature.time.dto.TimeRequest;
import site.secmega.secapi.feature.time.dto.TimeResponse;
import site.secmega.secapi.mapper.TimeMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TimeServiceImpl implements TimeService{

    private final TimeRepository timeRepository;
    private final TimeMapper timeMapper;

    @Override
    public TimeResponse createTime(TimeRequest timeRequest) {

        Time time = timeMapper.fromTimeRequest(timeRequest);
        Time savedTime = timeRepository.save(time);

        return timeMapper.toTimeResponse(savedTime);

    }

    @Override
    public List<TimeLookupResponse> findTimeLookup() {

        List<Time> times = timeRepository.findAll();

        return times.stream().map(timeMapper::toTimeLookupResponse).toList();
    }
}

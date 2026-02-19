package site.secmega.secapi.feature.tv;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.domain.Tv;
import site.secmega.secapi.feature.tv.dto.TvRequest;
import site.secmega.secapi.feature.tv.dto.TvResponse;
import site.secmega.secapi.mapper.TvMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TvServiceImpl implements TvService{

    private final TvRepository tvRepository;
    private final TvMapper tvMapper;

    @Override
    public TvResponse create(TvRequest tvRequest) {
        if (tvRepository.existsByName(tvRequest.name())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "TV name already exist");
        }
        Tv tv = tvMapper.fromTvRequest(tvRequest);
        tv.setCreatedAt(LocalDateTime.now());
        Tv savedTv = tvRepository.save(tv);
        return tvMapper.toTvResponse(savedTv);

    }

    @Override
    public List<TvResponse> findTv() {
        List<Tv> tvs = tvRepository.findAll();
        return tvs.stream().map(tvMapper::toTvResponse).toList();
    }
}

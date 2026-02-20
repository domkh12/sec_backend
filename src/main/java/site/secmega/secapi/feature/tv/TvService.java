package site.secmega.secapi.feature.tv;

import site.secmega.secapi.feature.tv.dto.TvDataRequest;
import site.secmega.secapi.feature.tv.dto.TvDataResponse;
import site.secmega.secapi.feature.tv.dto.TvRequest;
import site.secmega.secapi.feature.tv.dto.TvResponse;

import java.util.List;

public interface TvService {
    TvResponse create(TvRequest tvRequest);

    List<TvResponse> findTv();

    TvDataResponse getDataByTvName(String name);

    TvDataResponse createDataTv(TvDataRequest tvDataRequest);
}

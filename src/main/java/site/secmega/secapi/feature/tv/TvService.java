package site.secmega.secapi.feature.tv;

import site.secmega.secapi.feature.tv.dto.*;

import java.util.List;

public interface TvService {
    TvResponse create(TvRequest tvRequest);

    List<TvResponse> findTv();

    TvDataResponse getDataByTvName(String name);

    TvDataResponse createDataTv(TvDataRequest tvDataRequest);

    List<TvGeneralResponse> getTvGeneralData();
}

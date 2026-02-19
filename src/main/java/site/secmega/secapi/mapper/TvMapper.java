package site.secmega.secapi.mapper;

import org.mapstruct.Mapper;
import site.secmega.secapi.domain.Tv;
import site.secmega.secapi.feature.tv.dto.TvRequest;
import site.secmega.secapi.feature.tv.dto.TvResponse;

@Mapper(componentModel = "spring")
public interface TvMapper {
    Tv fromTvRequest(TvRequest tvRequest);
    TvResponse toTvResponse(Tv tv);
}

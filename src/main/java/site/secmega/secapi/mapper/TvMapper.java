package site.secmega.secapi.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import site.secmega.secapi.domain.Tv;
import site.secmega.secapi.domain.TvData;
import site.secmega.secapi.feature.tv.dto.TvDataRequest;
import site.secmega.secapi.feature.tv.dto.TvDataResponse;
import site.secmega.secapi.feature.tv.dto.TvRequest;
import site.secmega.secapi.feature.tv.dto.TvResponse;

@Mapper(componentModel = "spring")
public interface TvMapper {
    Tv fromTvRequest(TvRequest tvRequest);
    TvResponse toTvResponse(Tv tv);
    TvData fromTvDataRequest(TvDataRequest tvDataRequest);
    TvDataResponse toTvDataResponse(Tv tv);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTvFromTvDataRequest(TvDataRequest tvDataRequest, @MappingTarget Tv tv);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateTvDataFromTvDataRequest(TvDataRequest tvDataRequest, @MappingTarget TvData tvData);
}

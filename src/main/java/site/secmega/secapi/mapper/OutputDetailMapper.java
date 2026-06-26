package site.secmega.secapi.mapper;

import org.mapstruct.Mapper;
import site.secmega.secapi.domain.OutputDetail;
import site.secmega.secapi.feature.outputDetail.dto.OutputDetailRequest;
import site.secmega.secapi.feature.outputDetail.dto.OutputDetailResponse;

@Mapper(componentModel = "spring")
public interface OutputDetailMapper {
    OutputDetail fromOutputDetailRequest(OutputDetailRequest outputDetailRequest);
    OutputDetailResponse toOutputDetailResponse(OutputDetail outputDetail);
}

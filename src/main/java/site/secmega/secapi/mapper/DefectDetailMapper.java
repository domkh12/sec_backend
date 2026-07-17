package site.secmega.secapi.mapper;

import org.mapstruct.Mapper;
import site.secmega.secapi.domain.DefectDetail;
import site.secmega.secapi.feature.defectDetail.dto.DefectDetailResponse;

@Mapper(componentModel = "spring")
public interface DefectDetailMapper {
    DefectDetailResponse toDefectDetailResponse(DefectDetail defectDetail);
}

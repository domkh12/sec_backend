package site.secmega.secapi.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import site.secmega.secapi.domain.DefectType;
import site.secmega.secapi.feature.defectType.dto.DefectTypeRequest;
import site.secmega.secapi.feature.defectType.dto.DefectTypeResponse;

@Mapper(componentModel = "spring")
public interface DefectTypeMapper {
    DefectType fromDefectTypeRequest(DefectTypeRequest defectTypeRequest);
    DefectTypeResponse toDefectTypeResponse(DefectType defectType);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDefectTypeRequest(DefectTypeRequest defectTypeRequest, @MappingTarget DefectType defectType);
}

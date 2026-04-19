package site.secmega.secapi.feature.defectType;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import site.secmega.secapi.feature.defectType.dto.DefectTypeFilterRequest;
import site.secmega.secapi.feature.defectType.dto.DefectTypeRequest;
import site.secmega.secapi.feature.defectType.dto.DefectTypeResponse;

public interface DefectTypeService {
    DefectTypeResponse createDefectType(@Valid DefectTypeRequest defectTypeRequest);

    Page<DefectTypeResponse> findAll(DefectTypeFilterRequest defectTypeFilterRequest);

    DefectTypeResponse updateDefectType(Long id, @Valid DefectTypeRequest defectTypeRequest);

    void deleteDefectType(Long id);
}

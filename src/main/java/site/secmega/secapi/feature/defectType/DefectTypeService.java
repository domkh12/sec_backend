package site.secmega.secapi.feature.defectType;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import site.secmega.secapi.feature.defectType.dto.DefectTypeFilterRequest;
import site.secmega.secapi.feature.defectType.dto.DefectTypeLookupResponse;
import site.secmega.secapi.feature.defectType.dto.DefectTypeRequest;
import site.secmega.secapi.feature.defectType.dto.DefectTypeResponse;

import java.util.List;

public interface DefectTypeService {
    List<DefectTypeLookupResponse> getDefectTypeLookup();

    DefectTypeResponse createDefectType(@Valid DefectTypeRequest defectTypeRequest);

    Page<DefectTypeResponse> findAll(DefectTypeFilterRequest defectTypeFilterRequest);

    DefectTypeResponse updateDefectType(Long id, @Valid DefectTypeRequest defectTypeRequest);

    void deleteDefectType(Long id);
}

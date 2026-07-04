package site.secmega.secapi.feature.analysis.dto;

import lombok.Builder;
import site.secmega.secapi.feature.defectType.dto.DefectTypeWithQtyResponse;

import java.util.List;

@Builder
public record LineDefectResponse(
        String line,
        String buyer,
        String mo,
        String style,
        Integer output,
        Integer defect,
        List<DefectTypeWithQtyResponse> defectTypes
) {
}

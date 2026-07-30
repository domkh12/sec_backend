package site.secmega.secapi.feature.defectDetail;

import org.springframework.data.domain.Page;
import site.secmega.secapi.feature.defectDetail.dto.DefectDetailFilterRequest;
import site.secmega.secapi.feature.defectDetail.dto.DefectDetailResponse;

public interface DefectDetailService {

    void updateDefectQty(Long id, Integer qty);

    Page<DefectDetailResponse> findAll(DefectDetailFilterRequest defectDetailFilterRequest);

    void deleteDefect(Long id);
}

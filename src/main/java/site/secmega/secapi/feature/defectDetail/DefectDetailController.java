package site.secmega.secapi.feature.defectDetail;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.secmega.secapi.feature.defectDetail.dto.DefectDetailFilterRequest;
import site.secmega.secapi.feature.defectDetail.dto.DefectDetailResponse;

@RestController
@RequestMapping("/api/v1/defect-details")
@RequiredArgsConstructor
public class DefectDetailController {

    private final DefectDetailService defectDetailService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.CREATED)
    Page<DefectDetailResponse> findAll(@ModelAttribute DefectDetailFilterRequest defectDetailFilterRequest){
        return defectDetailService.findAll(defectDetailFilterRequest);
    }
}

package site.secmega.secapi.feature.defectType;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.secmega.secapi.feature.defectType.dto.DefectTypeFilterRequest;
import site.secmega.secapi.feature.defectType.dto.DefectTypeRequest;
import site.secmega.secapi.feature.defectType.dto.DefectTypeResponse;

@RestController
@RequestMapping("/api/v1/defect-types")
@RequiredArgsConstructor
public class DefectTypeController {

    private final DefectTypeService defectTypeService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteDefectType(@PathVariable Long id){
        defectTypeService.deleteDefectType(id);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    DefectTypeResponse updateDefectType(@PathVariable Long id, @RequestBody @Valid DefectTypeRequest defectTypeRequest){
        return defectTypeService.updateDefectType(id, defectTypeRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<DefectTypeResponse> findAll(@ModelAttribute DefectTypeFilterRequest defectTypeFilterRequest){
        return defectTypeService.findAll(defectTypeFilterRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    DefectTypeResponse createDefectType(@RequestBody @Valid DefectTypeRequest defectTypeRequest){
        return defectTypeService.createDefectType(defectTypeRequest);
    }

}

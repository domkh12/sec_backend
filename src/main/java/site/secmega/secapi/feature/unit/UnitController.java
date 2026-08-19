package site.secmega.secapi.feature.unit;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.secmega.secapi.feature.unit.dto.UnitFilterRequest;
import site.secmega.secapi.feature.unit.dto.UnitRequest;
import site.secmega.secapi.feature.unit.dto.UnitResponse;

@RestController
@RequestMapping("/api/v1/units")
@RequiredArgsConstructor
public class UnitController {

    private final UnitService unitService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WAREHOUSE')")
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUnit(@PathVariable String uuid){
        unitService.deleteUnit(uuid);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WAREHOUSE')")
    @PutMapping("/{uuid}")
    @ResponseStatus(HttpStatus.CREATED)
    UnitResponse updateUnit(@PathVariable String uuid, @Valid @RequestBody UnitRequest unitRequest){
        return unitService.updateUnit(uuid, unitRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WAREHOUSE')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    UnitResponse createUnit(@Valid @RequestBody UnitRequest unitRequest){
        return unitService.createUnit(unitRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WAREHOUSE')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<UnitResponse> findAll(@ModelAttribute UnitFilterRequest unitFilterRequest){
        return unitService.findAll(unitFilterRequest);
    }

}

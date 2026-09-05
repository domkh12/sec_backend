package site.secmega.secapi.feature.materialColor;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.secmega.secapi.feature.materialColor.dto.MaterialColorFilterResponse;
import site.secmega.secapi.feature.materialColor.dto.MaterialColorRequest;
import site.secmega.secapi.feature.materialColor.dto.MaterialColorResponse;

@RestController
@RequestMapping("/api/v1/material-colors")
@RequiredArgsConstructor
public class MaterialColorController {

    private final MaterialColorService materialColorService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_WAREHOUSE')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<MaterialColorResponse> findAll(@ModelAttribute MaterialColorFilterResponse materialColorFilterResponse){
        return materialColorService.findAll(materialColorFilterResponse);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_WAREHOUSE')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    MaterialColorResponse createMaterialColor(@RequestBody MaterialColorRequest materialColorRequest){
        return materialColorService.createMaterialColor(materialColorRequest);
    }

}

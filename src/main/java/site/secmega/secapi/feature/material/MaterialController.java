package site.secmega.secapi.feature.material;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.secmega.secapi.feature.material.dto.MaterialFilterRequest;
import site.secmega.secapi.feature.material.dto.MaterialRequest;
import site.secmega.secapi.feature.material.dto.MaterialResponse;

@RestController
@RequestMapping("/api/v1/materials")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService materialService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<MaterialResponse> findAll(@ModelAttribute MaterialFilterRequest materialFilterRequest){
        return materialService.findAll(materialFilterRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    MaterialResponse createMaterial(@RequestBody @Valid MaterialRequest materialRequest){
        return materialService.createMaterial(materialRequest);
    }

}

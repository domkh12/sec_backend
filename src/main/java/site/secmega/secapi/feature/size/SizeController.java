package site.secmega.secapi.feature.size;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.secmega.secapi.feature.size.dto.SizeFilterRequest;
import site.secmega.secapi.feature.size.dto.SizeLookupResponse;
import site.secmega.secapi.feature.size.dto.SizeRequest;
import site.secmega.secapi.feature.size.dto.SizeResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sizes")
@RequiredArgsConstructor
public class SizeController {

    private final SizeService sizeService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("/lookup")
    @ResponseStatus(HttpStatus.OK)
    List<SizeLookupResponse> getSizeLookup(){
        return sizeService.getSizeLookup();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteSize(@PathVariable Long id){
        sizeService.deleteSize(id);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    SizeResponse updateSize(@PathVariable Long id, @Valid @RequestBody SizeRequest sizeRequest){
        return sizeService.updateSize(id, sizeRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<SizeResponse> findAll(@ModelAttribute SizeFilterRequest sizeFilterRequest){
        return sizeService.findAll(sizeFilterRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    SizeResponse createSize(@Valid @RequestBody SizeRequest sizeRequest){
        return sizeService.createService(sizeRequest);
    }

}

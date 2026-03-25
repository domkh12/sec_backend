package site.secmega.secapi.feature.size;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.secmega.secapi.feature.size.dto.SizeFilterRequest;
import site.secmega.secapi.feature.size.dto.SizeRequest;
import site.secmega.secapi.feature.size.dto.SizeResponse;

@RestController
@RequestMapping("/api/v1/sizes")
@RequiredArgsConstructor
public class SizeController {

    private final SizeService sizeService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteSize(@PathVariable Long id){
        sizeService.deleteSize(id);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    SizeResponse updateSize(@PathVariable Long id, @Valid @RequestBody SizeRequest sizeRequest){
        return sizeService.updateSize(id, sizeRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<SizeResponse> findAll(@ModelAttribute SizeFilterRequest sizeFilterRequest){
        return sizeService.findAll(sizeFilterRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    SizeResponse createSize(@Valid @RequestBody SizeRequest sizeRequest){
        return sizeService.createService(sizeRequest);
    }

}

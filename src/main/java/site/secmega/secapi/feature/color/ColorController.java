package site.secmega.secapi.feature.color;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.secmega.secapi.feature.color.dto.ColorFilterRequest;
import site.secmega.secapi.feature.color.dto.ColorRequest;
import site.secmega.secapi.feature.color.dto.ColorResponse;

@RestController
@RequestMapping("/api/v1/colors")
@RequiredArgsConstructor
public class ColorController {

    private final ColorService colorService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteColor(@PathVariable Long id){
        colorService.deleteColor(id);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    ColorResponse updateColor(@PathVariable Long id, @Valid @RequestBody ColorRequest colorRequest){
        return colorService.updateColor(id, colorRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ColorResponse createColor(@Valid @RequestBody ColorRequest colorRequest){
        return colorService.createColor(colorRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<ColorResponse> findAll(@ModelAttribute ColorFilterRequest colorFilterRequest){
        return colorService.findAll(colorFilterRequest);
    }

}

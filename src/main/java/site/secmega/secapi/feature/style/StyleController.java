package site.secmega.secapi.feature.style;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import lombok.RequiredArgsConstructor;
import site.secmega.secapi.feature.style.dto.StyleFilterRequest;
import site.secmega.secapi.feature.style.dto.StyleRequest;
import site.secmega.secapi.feature.style.dto.StyleResponse;
import site.secmega.secapi.feature.style.dto.StyleStateResponse;

@RestController
@RequestMapping("/api/v1/styles")
@RequiredArgsConstructor
public class StyleController {

    private final StyleService styleService;

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    StyleStateResponse getStyleState(){
        return styleService.getStyleState();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteStyle(@PathVariable Long id){
        styleService.deleteStyle(id);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    StyleResponse updateStyle(@PathVariable Long id, @Valid @RequestBody StyleRequest styleRequest){
        return styleService.updateStyle(id, styleRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    StyleResponse createStyle(@RequestBody @Valid StyleRequest styleRequest){
        return styleService.createStyle(styleRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_MANAGER', 'ROLE_ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<StyleResponse> getStyle(@ModelAttribute StyleFilterRequest styleFilterRequest) {
        return styleService.getStyle(styleFilterRequest);
    }
    
}

package site.secmega.secapi.feature.category;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.secmega.secapi.feature.category.dto.CategoryFilterRequest;
import site.secmega.secapi.feature.category.dto.CategoryLookupResponse;
import site.secmega.secapi.feature.category.dto.CategoryRequest;
import site.secmega.secapi.feature.category.dto.CategoryResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/lookup")
    @ResponseStatus(HttpStatus.OK)
    List<CategoryLookupResponse> getCategoryLookup(){
        return categoryService.getCategoryLookup();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteCategory(@PathVariable Long id){
        categoryService.deleteCategory(id);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    CategoryResponse updateCategory(@PathVariable Long id, @Valid @RequestBody CategoryRequest categoryRequest){
        return categoryService.updateCategory(id, categoryRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    CategoryResponse createCategory(@Valid @RequestBody CategoryRequest categoryRequest){
        return categoryService.createCategory(categoryRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<CategoryResponse> findAll(@ModelAttribute CategoryFilterRequest categoryFilterRequest){
        return categoryService.findAll(categoryFilterRequest);
    }

}

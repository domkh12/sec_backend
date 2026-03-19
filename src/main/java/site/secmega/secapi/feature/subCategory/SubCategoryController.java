package site.secmega.secapi.feature.subCategory;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.secmega.secapi.feature.subCategory.dto.SubCategoryFilterRequest;
import site.secmega.secapi.feature.subCategory.dto.SubCategoryRequest;
import site.secmega.secapi.feature.subCategory.dto.SubCategoryResponse;

@RestController
@RequestMapping("/api/v1/sub-categories")
@RequiredArgsConstructor
public class SubCategoryController {
    private final SubCategoryService subCategoryService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteSubCategory(@PathVariable Long id){
        subCategoryService.deleteSubCategory(id);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    SubCategoryResponse updateSubCategory(@PathVariable Long id, @Valid @RequestBody SubCategoryRequest subCategoryRequest){
        return subCategoryService.updateSubCategory(id, subCategoryRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    SubCategoryResponse createSubCategory(@Valid @RequestBody SubCategoryRequest subCategoryRequest){
        return subCategoryService.createSubCategory(subCategoryRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<SubCategoryResponse> findAll(@ModelAttribute SubCategoryFilterRequest subCategoryFilterRequest){
        return subCategoryService.findAll(subCategoryFilterRequest);
    }
}

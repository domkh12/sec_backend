package site.secmega.secapi.feature.subCategory;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import site.secmega.secapi.feature.subCategory.dto.SubCategoryFilterRequest;
import site.secmega.secapi.feature.subCategory.dto.SubCategoryRequest;
import site.secmega.secapi.feature.subCategory.dto.SubCategoryResponse;

public interface SubCategoryService {
    Page<SubCategoryResponse> findAll(SubCategoryFilterRequest subCategoryFilterRequest);

    SubCategoryResponse createSubCategory(@Valid SubCategoryRequest subCategoryRequest);

    SubCategoryResponse updateSubCategory(Long id, @Valid SubCategoryRequest subCategoryRequest);

    void deleteSubCategory(Long id);
}

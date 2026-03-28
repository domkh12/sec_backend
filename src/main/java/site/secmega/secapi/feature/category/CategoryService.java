package site.secmega.secapi.feature.category;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import site.secmega.secapi.feature.category.dto.CategoryFilterRequest;
import site.secmega.secapi.feature.category.dto.CategoryLookupResponse;
import site.secmega.secapi.feature.category.dto.CategoryRequest;
import site.secmega.secapi.feature.category.dto.CategoryResponse;

import java.util.List;

public interface CategoryService {
    Page<CategoryResponse> findAll(CategoryFilterRequest categoryFilterRequest);

    CategoryResponse createCategory(@Valid CategoryRequest categoryRequest);

    CategoryResponse updateCategory(Long id, @Valid CategoryRequest categoryRequest);

    void deleteCategory(Long id);

    List<CategoryLookupResponse> getCategoryLookup();
}

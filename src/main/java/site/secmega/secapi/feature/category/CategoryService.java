package site.secmega.secapi.feature.category;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import site.secmega.secapi.feature.category.dto.CategoryRequest;
import site.secmega.secapi.feature.category.dto.CategoryResponse;

public interface CategoryService {
    Page<CategoryResponse> findAll(Integer pageNo, Integer pageSize);

    CategoryResponse createCategory(@Valid CategoryRequest categoryRequest);
}

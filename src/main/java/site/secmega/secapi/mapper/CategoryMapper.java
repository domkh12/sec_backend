package site.secmega.secapi.mapper;

import org.mapstruct.Mapper;
import site.secmega.secapi.domain.Category;
import site.secmega.secapi.feature.category.dto.CategoryRequest;
import site.secmega.secapi.feature.category.dto.CategoryResponse;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    CategoryResponse toCategoryResponse(Category category);
    Category fromCategoryRequest(CategoryRequest categoryRequest);
}

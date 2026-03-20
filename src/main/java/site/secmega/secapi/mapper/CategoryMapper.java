package site.secmega.secapi.mapper;

import org.mapstruct.*;
import site.secmega.secapi.domain.Category;
import site.secmega.secapi.domain.SubCategory;
import site.secmega.secapi.feature.category.dto.CategoryRequest;
import site.secmega.secapi.feature.category.dto.CategoryResponse;
import site.secmega.secapi.feature.subCategory.dto.SubCategoryResponse;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    @Mapping(target = "subCategories", source = "subCategories")
    @Mapping(target = "items", expression = "java(category.getSubCategories() != null ? category.getSubCategories().stream().mapToInt(sub -> sub.getProducts() != null ? sub.getProducts().size() : 0).sum() : null)")
    CategoryResponse toCategoryResponse(Category category);
    @Mapping(target = "category", source = "category.name")   // extract name from Category object
    @Mapping(target = "items", expression = "java(subCategory.getProducts() != null ? subCategory.getProducts().size() : null)")
    SubCategoryResponse toSubCategoryResponse(SubCategory subCategory);
    Category fromCategoryRequest(CategoryRequest categoryRequest);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromCategoryRequest(CategoryRequest categoryRequest, @MappingTarget Category category);
}

package site.secmega.secapi.mapper;

import org.mapstruct.*;
import site.secmega.secapi.domain.SubCategory;
import site.secmega.secapi.feature.subCategory.dto.SubCategoryRequest;
import site.secmega.secapi.feature.subCategory.dto.SubCategoryResponse;

@Mapper(componentModel = "spring")
public interface SubCategoryMapper {
    @Mapping(target = "category", expression = "java(subCategory.getCategory() != null ? subCategory.getCategory().getName() : null)")
    SubCategoryResponse toSubCategoryResponse(SubCategory subCategory);

    SubCategory fromSubCategoryRequest(SubCategoryRequest subCategoryRequest);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromSubCategoryRequest(SubCategoryRequest subCategoryRequest, @MappingTarget SubCategory subCategory);
}

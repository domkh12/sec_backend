package site.secmega.secapi.feature.category.dto;

import site.secmega.secapi.feature.subCategory.dto.SubCategoryResponse;

import java.util.List;

public record CategoryResponse(
        Long id,
        String name,
        Integer items,
        List<SubCategoryResponse> subCategories
) {
}

package site.secmega.secapi.feature.category.dto;

import lombok.Builder;
import site.secmega.secapi.feature.subCategory.dto.SubCategoryLookupResponse;

import java.util.List;

@Builder
public record CategoryLookupResponse(
        Long id,
        String name,
        List<SubCategoryLookupResponse> subCategories
) {
}

package site.secmega.secapi.feature.subCategory.dto;

import lombok.Builder;

@Builder
public record SubCategoryLookupResponse(
        Long id,
        String name
) {
}

package site.secmega.secapi.feature.subCategory.dto;

public record SubCategoryResponse(
        Long id,
        String name,
        Integer items,
        String category
) {
}

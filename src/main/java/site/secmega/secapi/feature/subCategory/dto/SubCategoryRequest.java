package site.secmega.secapi.feature.subCategory.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record SubCategoryRequest(
        @NotBlank(message = "Name is required")
        String name,
        @NotNull(message = "Category ID cannot be null")
        Long categoryId
) {
}

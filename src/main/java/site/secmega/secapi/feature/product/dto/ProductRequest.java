package site.secmega.secapi.feature.product.dto;

import jakarta.validation.constraints.NotNull;

public record ProductRequest(
    String styleName,
    String code,
    String size,
    String color,
    @NotNull(message = "Sub Category ID cannot be null")
    Long subCategoryId
) {
    
}

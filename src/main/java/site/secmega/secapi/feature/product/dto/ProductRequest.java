package site.secmega.secapi.feature.product.dto;

import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ProductRequest(
        String styleNo,
        @NotNull(message = "Size ID cannot be null")
        List<Long> sizeId,
        @NotNull(message = "Color ID cannot be null")
        List<Long> colorId,
        Long subCategoryId
) {
    
}

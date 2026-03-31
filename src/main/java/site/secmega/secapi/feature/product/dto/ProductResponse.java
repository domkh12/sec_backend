package site.secmega.secapi.feature.product.dto;

import site.secmega.secapi.base.ProductStatus;

import java.util.List;

public record ProductResponse(
    Long id,
    String styleNo,
    String size,
    String color,
    Integer targetProductionPerHour,
    Integer targetProductionPerDay,
    Double standardMinuteValue,
    String description,
    String code,
    Long categoryId,
    String category,
    Long subCategoryId,
    String subCategory,
    ProductStatus status,
    List<Long> colorId,
    List<Long> sizeId
) {
    
}

package site.secmega.secapi.feature.product.dto;

import site.secmega.secapi.base.ProductStatus;

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
    Long cateId,
    String category,
    String subCategoryId,
    String subCategory,
    ProductStatus status
) {
    
}

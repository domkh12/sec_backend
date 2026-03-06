package site.secmega.secapi.feature.product.dto;

public record ProductResponse(
    Long id,
    String name,
    String category,
    String size,
    String color,
    Integer targetProductionPerHour,
    Integer targetProductionPerDay,
    Double standardMinuteValue,
    String description,
    String code
) {
    
}

package site.secmega.secapi.feature.product.dto;

public record ProductRequest(
    String name,
    String code,
    String size,
    String color,
    String category
) {
    
}

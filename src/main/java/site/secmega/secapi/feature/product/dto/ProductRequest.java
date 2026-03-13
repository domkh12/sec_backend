package site.secmega.secapi.feature.product.dto;

public record ProductRequest(
    String styleName,
    String code,
    String size,
    String color,
    String category
) {
    
}

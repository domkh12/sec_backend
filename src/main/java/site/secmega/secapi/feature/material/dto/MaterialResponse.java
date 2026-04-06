package site.secmega.secapi.feature.material.dto;

public record MaterialResponse(
        Long id,
        String code,
        Double pricePerUnit,
        String name,
        String description,
        String unit,
        String image
) {
}

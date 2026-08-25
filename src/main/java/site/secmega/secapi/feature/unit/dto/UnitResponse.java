package site.secmega.secapi.feature.unit.dto;

public record UnitResponse(
        Long id,
        String uuid,
        String unitCode,
        String unitName,
        String description
) {
}

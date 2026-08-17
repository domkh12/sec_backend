package site.secmega.secapi.feature.warehouse.dto;

public record WarehouseLookupResponse(
        Long id,
        String uuid,
        String code,
        String name
) {
}

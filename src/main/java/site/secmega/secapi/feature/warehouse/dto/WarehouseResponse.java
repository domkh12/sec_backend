package site.secmega.secapi.feature.warehouse.dto;

public record WarehouseResponse(
        Long id,
        String uuid,
        String code,
        String name,
        String address,
        String city
) {
}

package site.secmega.secapi.feature.warehouse.dto;

public record WarehouseRequest(
        String code,
        String name,
        String address,
        String city
) {
}

package site.secmega.secapi.feature.rack.dto;

import site.secmega.secapi.feature.warehouse.dto.WarehouseLookupResponse;

public record RackResponse(
    Long id,
    String uuid,
    String code,
    Boolean isActive,
    WarehouseLookupResponse warehouse
) {
}

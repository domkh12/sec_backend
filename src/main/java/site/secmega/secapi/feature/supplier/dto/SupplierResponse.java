package site.secmega.secapi.feature.supplier.dto;

public record SupplierResponse(
        Long id,
        String uuid,
        String supplierName,
        String contactPerson,
        String phone,
        String email,
        String address
) {
}

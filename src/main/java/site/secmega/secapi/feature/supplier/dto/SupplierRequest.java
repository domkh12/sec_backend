package site.secmega.secapi.feature.supplier.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record SupplierRequest(
    @NotBlank(message = "Supplier is required!")
    String supplierName,
    String contactPerson,
    String phone,
    @Email(message = "Invalid Email!")
    String email,
    String address
) {
}

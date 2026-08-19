package site.secmega.secapi.feature.supplier;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import site.secmega.secapi.feature.supplier.dto.SupplierFilterRequest;
import site.secmega.secapi.feature.supplier.dto.SupplierRequest;
import site.secmega.secapi.feature.supplier.dto.SupplierResponse;

public interface SupplierService {
    Page<SupplierResponse> findAll(SupplierFilterRequest supplierFilterRequest);

    SupplierResponse createSupplier(@Valid SupplierRequest supplierRequest);
}

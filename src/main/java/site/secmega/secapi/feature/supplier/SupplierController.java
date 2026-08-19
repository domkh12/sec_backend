package site.secmega.secapi.feature.supplier;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.secmega.secapi.feature.supplier.dto.SupplierFilterRequest;
import site.secmega.secapi.feature.supplier.dto.SupplierRequest;
import site.secmega.secapi.feature.supplier.dto.SupplierResponse;
import site.secmega.secapi.feature.unit.dto.UnitFilterRequest;
import site.secmega.secapi.feature.unit.dto.UnitResponse;

@RestController
@RequestMapping("/api/v1/suppliers")
@RequiredArgsConstructor
public class SupplierController {

    private final SupplierService supplierService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WAREHOUSE')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    SupplierResponse createSupplier(@Valid @RequestBody SupplierRequest supplierRequest){
        return supplierService.createSupplier(supplierRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WAREHOUSE')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<SupplierResponse> findAll(@ModelAttribute SupplierFilterRequest supplierFilterRequest){
        return supplierService.findAll(supplierFilterRequest);
    }


}

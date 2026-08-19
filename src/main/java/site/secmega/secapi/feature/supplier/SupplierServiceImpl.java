package site.secmega.secapi.feature.supplier;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.domain.Supplier;
import site.secmega.secapi.feature.supplier.dto.SupplierFilterRequest;
import site.secmega.secapi.feature.supplier.dto.SupplierRequest;
import site.secmega.secapi.feature.supplier.dto.SupplierResponse;
import site.secmega.secapi.mapper.SupplierMapper;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SupplierServiceImpl implements SupplierService{

    private final SupplierRepository supplierRepository;
    private final SupplierMapper supplierMapper;

    @Override
    public SupplierResponse createSupplier(SupplierRequest supplierRequest) {

        Supplier supplier = supplierMapper.fromSupplierRequest(supplierRequest);
        supplier.setUuid(UUID.randomUUID().toString());
        Supplier savedSupplier = supplierRepository.save(supplier);

        return supplierMapper.toSupplierResponse(savedSupplier);
    }

    @Override
    public Page<SupplierResponse> findAll(SupplierFilterRequest supplierFilterRequest) {

        if (supplierFilterRequest.pageNo() <= 0 || supplierFilterRequest.pageSize() <= 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page no or Page size must bigger than 0!");
        }

        Specification<Supplier> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (supplierFilterRequest.search() != null){
            String searchTerm = "%" + supplierFilterRequest.search().toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> cb.or(
                    cb.like(cb.lower(root.get("contactPerson")), searchTerm),
                    cb.like(cb.lower(root.get("supplierName")), searchTerm),
                    cb.like(cb.lower(root.get("phone")), searchTerm),
                    cb.like(cb.lower(root.get("email")), searchTerm),
                    cb.like(cb.lower(root.get("address")), searchTerm)
            ));
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(supplierFilterRequest.pageNo() - 1, supplierFilterRequest.pageSize(), sort);
        Page<Supplier> suppliers = supplierRepository.findAll(spec, pageRequest);

        return suppliers.map(supplierMapper::toSupplierResponse);
    }


}

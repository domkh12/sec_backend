package site.secmega.secapi.feature.receipt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.domain.PurchaseOrder;
import site.secmega.secapi.domain.Receipt;
import site.secmega.secapi.domain.Supplier;
import site.secmega.secapi.feature.purchaseOrder.PurchaseOrderRepository;
import site.secmega.secapi.feature.receipt.dto.ReceiptFilterRequest;
import site.secmega.secapi.feature.receipt.dto.ReceiptRequest;
import site.secmega.secapi.feature.receipt.dto.ReceiptResponse;
import site.secmega.secapi.feature.supplier.SupplierRepository;
import site.secmega.secapi.mapper.ReceiptMapper;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ReceiptServiceImpl implements ReceiptService{

    private final ReceiptRepository receiptRepository;
    private final ReceiptMapper receiptMapper;
    private final SupplierRepository supplierRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;

    @Override
    public Page<ReceiptResponse> findAll(ReceiptFilterRequest receiptFilterRequest) {

        if (receiptFilterRequest.pageNo() <= 0 || receiptFilterRequest.pageSize() <= 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page no or Page size must bigger than 0");
        }

        Specification<Receipt> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (StringUtils.hasText(receiptFilterRequest.search())){
            String searchTerm = "%" + receiptFilterRequest.search().trim().toLowerCase() + "%";
            spec = spec.and((root, query, cb) ->
                    cb.or(
                        cb.like(cb.lower(root.get("receiptNo")),  searchTerm)
                    ));
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(receiptFilterRequest.pageNo() - 1, receiptFilterRequest.pageSize(), sort);
        Page<Receipt> receipts = receiptRepository.findAll(spec, pageRequest);

        return receipts.map(receiptMapper::toReceiptResponse);
    }

    @Override
    public ReceiptResponse updateReceipt(String uuid, ReceiptRequest receiptRequest) {

        Receipt receipt = receiptRepository.findByUuidAndDeletedAtNull(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Receipt not found!")
        );

        if (receiptRepository.existsByReceiptNoAndUuidNotAndDeletedAtNull(receiptRequest.receiptNo(), uuid)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Receipt already exist!");
        }

        receiptMapper.updateFromReceiptRequest(receiptRequest, receipt);

        if (StringUtils.hasText(receiptRequest.supplierUuid())){
            receipt.setSupplier(supplierRepository.findByUuid(receiptRequest.supplierUuid()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Supplier not found!")
            ));
        }

        if (StringUtils.hasText(receiptRequest.poUuid())){
            receipt.setPurchaseOrder(purchaseOrderRepository.findByUuid(receiptRequest.poUuid()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Supplier not found!")
            ));
        }

        Receipt savedReceipt = receiptRepository.save(receipt);
        return receiptMapper.toReceiptResponse(savedReceipt);
    }

    @Override
    public void deleteReceipt(String uuid) {
        Receipt receipt = receiptRepository.findByUuidAndDeletedAtNull(uuid).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Receipt not found!")
        );
        receipt.setDeletedAt(LocalDateTime.now());
        receiptRepository.save(receipt);
    }

    @Override
    public ReceiptResponse createReceipt(ReceiptRequest receiptRequest) {

        if (receiptRepository.existsByReceiptNoAndDeletedAtNull(receiptRequest.receiptNo())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Receipt already exist!");
        }

        Supplier supplier = supplierRepository.findByUuid(receiptRequest.supplierUuid()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Supplier not found!")
        );

        PurchaseOrder purchaseOrder = purchaseOrderRepository.findByUuid(receiptRequest.poUuid()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Purchase order not found!")
        );

        Receipt receipt = receiptMapper.fromReceiptRequest(receiptRequest);
        receipt.setUuid(UUID.randomUUID().toString());
        receipt.setSupplier(supplier);
        receipt.setPurchaseOrder(purchaseOrder);
        Receipt savedReceipt = receiptRepository.save(receipt);
        return receiptMapper.toReceiptResponse(savedReceipt);
    }

}

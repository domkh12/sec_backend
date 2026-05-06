package site.secmega.secapi.feature.purchaseOrder;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.base.POStatus;
import site.secmega.secapi.domain.ProductionLine;
import site.secmega.secapi.domain.PurchaseOrder;
import site.secmega.secapi.feature.purchaseOrder.dto.PurchaseOrderFilterRequest;
import site.secmega.secapi.feature.purchaseOrder.dto.PurchaseOrderRequest;
import site.secmega.secapi.feature.purchaseOrder.dto.PurchaseOrderResponse;
import site.secmega.secapi.feature.style.StyleRepository;
import site.secmega.secapi.mapper.PurchaseOrderMapper;

@Service
@RequiredArgsConstructor
public class PurchaseOrderServiceImpl implements PurchaseOrderService {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderMapper purchaseOrderMapper;
    private final StyleRepository styleRepository;

    @Override
    public Page<PurchaseOrderResponse> getPO(PurchaseOrderFilterRequest purchaseOrderFilterRequest) {

        if (purchaseOrderFilterRequest.pageSize() <= 0 || purchaseOrderFilterRequest.pageNo() <= 0){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page Size and Page No must be greater then 0");
        }

        Specification<PurchaseOrder> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (purchaseOrderFilterRequest.search() != null){
            String searchTerm = "%" + purchaseOrderFilterRequest.search().toLowerCase() + "%";
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("po")), searchTerm)
            );
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(purchaseOrderFilterRequest.pageNo() - 1, purchaseOrderFilterRequest.pageSize(), sort);
        Page<PurchaseOrder> po = purchaseOrderRepository.findAll(spec, pageRequest);

        return po.map(purchaseOrderMapper::toPurchaseOrderResponse);
    }

    @Override
    public PurchaseOrderResponse createPO(PurchaseOrderRequest purchaseOrderRequest) {

        if (purchaseOrderRepository.existsByPoIgnoreCaseAndDeletedAtNull(purchaseOrderRequest.po())){
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "PO already exist!"
            );
        }

        PurchaseOrder po = purchaseOrderMapper.fromPurchaseOrderRequest(purchaseOrderRequest);
        po.setStyle(styleRepository.findById(purchaseOrderRequest.styleId()).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Style not found!")
        ));
        po.setStatus(POStatus.PENDING);
        PurchaseOrder savedPO = purchaseOrderRepository.save(po);

        return purchaseOrderMapper.toPurchaseOrderResponse(savedPO);
    }

}

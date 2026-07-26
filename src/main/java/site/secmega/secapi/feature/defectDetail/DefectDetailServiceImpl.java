package site.secmega.secapi.feature.defectDetail;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import site.secmega.secapi.domain.DefectDetail;
import site.secmega.secapi.feature.buyer.dto.BuyerLookupResponse;
import site.secmega.secapi.feature.defectDetail.dto.DefectDetailFilterRequest;
import site.secmega.secapi.feature.defectDetail.dto.DefectDetailResponse;
import site.secmega.secapi.feature.productionLine.dto.ProductionLineLookupResponse;
import site.secmega.secapi.feature.purchaseOrder.dto.PurchaseOrderLookupResponse;
import site.secmega.secapi.feature.style.dto.StyleLookupResponse;
import site.secmega.secapi.feature.time.dto.TimeResponse;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DefectDetailServiceImpl implements DefectDetailService {

    private final DefectDetailRepository defectDetailRepository;

    @Override
    public void deleteDefect(Long id) {
        DefectDetail defectDetail = defectDetailRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Defect Detail not found!")
        );

        defectDetail.setDeletedAt(LocalDateTime.now());
        defectDetailRepository.save(defectDetail);

    }

    @Override
    public Page<DefectDetailResponse> findAll(DefectDetailFilterRequest defectDetailFilterRequest) {

        if(defectDetailFilterRequest.pageNo() <= 0)  throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page no and Page size must be greater than 0");

        if(defectDetailFilterRequest.pageSize() <= 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page no and Page size must be greater than 0");

        Specification<DefectDetail> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (defectDetailFilterRequest.search() != null){
            String searchTerm = "%" + defectDetailFilterRequest.search().toLowerCase() + "%";
            spec = spec.and((root, query, cb) ->
                            cb.or(
                                cb.like(cb.lower(root.get("workOrder").get("mo")), searchTerm),
                                cb.like(cb.lower(root.get("workOrder").get("purchaseOrder").get("po")), searchTerm),
                                cb.like(cb.lower(root.get("workOrder").get("purchaseOrder").get("style").get("styleNo")), searchTerm)
                            )
                           );
        }

        if (defectDetailFilterRequest.buyerId() != null){
            spec = spec.and((root, query, cb) -> cb.equal(root.get("workOrder").get("purchaseOrder").get("buyer").get("id"), defectDetailFilterRequest.buyerId()));
        }

        if (defectDetailFilterRequest.lineId() != null){
            spec = spec.and((root, query, cb) -> cb.equal(root.get("productionLine").get("id"), defectDetailFilterRequest.lineId()));
        }

        if (defectDetailFilterRequest.reportDate() != null){
            spec = spec.and((root, query, cb) -> cb.equal(root.get("defectDate"), defectDetailFilterRequest.reportDate()));
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(defectDetailFilterRequest.pageNo() - 1, defectDetailFilterRequest.pageSize(), sort);
        Page<DefectDetail> defectDetails = defectDetailRepository.findAll(spec, pageRequest);


        return new PageImpl<>(
                defectDetails.stream()
                        .map(defectDetail -> {
                            var workOrder = defectDetail.getWorkOrder();
                            var purchaseOrder = workOrder.getPurchaseOrder();
                            var buyer = purchaseOrder.getBuyer();

                            return DefectDetailResponse.builder()
                                    .id(defectDetail.getId())
                                    .reportDate(defectDetail.getCreatedAt())
                                    .mo(workOrder.getMo())
                                    .defectDate(defectDetail.getDefectDate())
                                    .defectQty(defectDetail.getDefectQty())

                                    .style(
                                            purchaseOrder.getStyle() != null
                                                    ? StyleLookupResponse.builder()
                                                    .id(purchaseOrder.getStyle().getId())
                                                    .styleNo(purchaseOrder.getStyle().getStyleNo())
                                                    .build()
                                                    : null
                                    )

                                    .line(
                                            defectDetail.getProductionLine() != null
                                                    ? ProductionLineLookupResponse.builder()
                                                    .id(defectDetail.getProductionLine().getId())
                                                    .line(defectDetail.getProductionLine().getLine())
                                                    .build()
                                                    : null
                                    )

                                    .purchaseOrder(
                                            PurchaseOrderLookupResponse.builder()
                                                    .id(purchaseOrder.getId())
                                                    .po(purchaseOrder.getPo())
                                                    .build()
                                    )

                                    .buyer(
                                            buyer != null
                                                    ? BuyerLookupResponse.builder()
                                                    .id(buyer.getId())
                                                    .name(buyer.getName())
                                                    .build()
                                                    : null
                                    )

                                    .time(
                                            defectDetail.getTime() != null
                                                    ? TimeResponse.builder()
                                                    .id(defectDetail.getTime().getId())
                                                    .name(defectDetail.getTime().getName())
                                                    .build()
                                                    : null
                                    )
                                    .build();
                        })
                        .toList(),
                pageRequest,
                defectDetails.getTotalElements()
        );
    }

}

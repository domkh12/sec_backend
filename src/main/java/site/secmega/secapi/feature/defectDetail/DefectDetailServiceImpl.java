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

@Service
@RequiredArgsConstructor
public class DefectDetailServiceImpl implements DefectDetailService {

    private final DefectDetailRepository defectDetailRepository;
    
    @Override
    public Page<DefectDetailResponse> findAll(DefectDetailFilterRequest defectDetailFilterRequest) {

        if(defectDetailFilterRequest.pageNo() <= 0)  throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page no and Page size must be greater than 0");
     
        if(defectDetailFilterRequest.pageSize() <= 0) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page no and Page size must be greater than 0");

        Specification<DefectDetail> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (defectDetailFilterRequest.search() != null){
            String searchTerm = "%" + defectDetailFilterRequest.search().toLowerCase() + "%";
            spec = spec.and((root, query, cb) -> 
                            cb.or(
                                cb.like(cb.lower(root.get("name")), searchTerm),
                                cb.like(cb.lower(root.get("workOrder").get("mo")), searchTerm),
                                cb.like(cb.lower(root.get("workOrder").get("purchaseOrder").get("po")), searchTerm),
                                cb.like(cb.lower(root.get("workOrder").get("purchaseOrder").get("style").get("styleNo")), searchTerm)
                            )
                           );
        }
        
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(defectDetailFilterRequest.pageNo() - 1, defectDetailFilterRequest.pageSize(), sort);
        Page<DefectDetail> defectDetails = defectDetailRepository.findAll(spec, pageRequest);
        

        return new PageImpl<>(
                defectDetails.stream().map(defectDetail ->
                        DefectDetailResponse.builder()
                                .id(defectDetail.getId())
                                .reportDate(defectDetail.getCreatedAt())
                                .mo(defectDetail.getWorkOrder().getMo())
                                .defectDate(defectDetail.getDefectDate())
                                .defectQty(defectDetail.getDefectQty())
                                .style(StyleLookupResponse.builder()
                                        .id(defectDetail.getWorkOrder().getPurchaseOrder().getStyle().getId())
                                        .styleNo(defectDetail.getWorkOrder().getPurchaseOrder().getStyle().getStyleNo())
                                        .build())
                                .line(ProductionLineLookupResponse.builder()
                                        .id(defectDetail.getProductionLine().getId())
                                        .line(defectDetail.getProductionLine().getLine())
                                        .build())
                                .purchaseOrder(PurchaseOrderLookupResponse.builder()
                                        .id(defectDetail.getWorkOrder().getPurchaseOrder().getId())
                                        .po(defectDetail.getWorkOrder().getPurchaseOrder().getPo())
                                        .build())
                                .buyer(BuyerLookupResponse.builder()
                                        .id(defectDetail.getWorkOrder().getPurchaseOrder().getBuyer().getId())
                                        .name(defectDetail.getWorkOrder().getPurchaseOrder().getBuyer().getName())
                                        .build())
                                .time(TimeResponse.builder()
                                        .id(defectDetail.getTime().getId())
                                        .name(defectDetail.getTime().getName())
                                        .build())
                                .build()
                ).toList(),
                pageRequest,
                defectDetails.getTotalElements()
        );
    }

}

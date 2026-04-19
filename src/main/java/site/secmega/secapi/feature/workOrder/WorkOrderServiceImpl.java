package site.secmega.secapi.feature.workOrder;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.base.WorkOrderStatus;
import site.secmega.secapi.domain.*;
import site.secmega.secapi.feature.buyer.BuyerRepository;
import site.secmega.secapi.feature.color.ColorRepository;
import site.secmega.secapi.feature.processDetail.ProcessingDetailRepository;
import site.secmega.secapi.feature.size.SizeRepository;
import site.secmega.secapi.feature.user.UserRepository;
import site.secmega.secapi.feature.workOrder.dto.WorkOrderFilterRequest;
import site.secmega.secapi.feature.workOrder.dto.WorkOrderLookupResponse;
import site.secmega.secapi.feature.workOrder.dto.WorkOrderRequest;
import site.secmega.secapi.feature.workOrder.dto.WorkOrderResponse;
import site.secmega.secapi.feature.workOrderColor.dto.WorkOrderColorResponse;
import site.secmega.secapi.feature.workOrderColorSize.dto.WorkOrderColorSizeResponse;
import site.secmega.secapi.feature.workOrderDetail.WorkOrderDetailRepository;
import site.secmega.secapi.mapper.WorkOrderMapper;
import site.secmega.secapi.util.AuthUtil;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class WorkOrderServiceImpl implements WorkOrderService{

    private final WorkOrderRepository workOrderRepository;
    private final WorkOrderMapper workOrderMapper;
    private final BuyerRepository buyerRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;
    private final AuthUtil authUtil;
    private final UserRepository userRepository;

    @Override
    public WorkOrderResponse createWorkOrder(WorkOrderRequest workOrderRequest) {
        Long userId = authUtil.loggedUserId();
        User user = userRepository.findById(userId).orElseThrow();
        if (workOrderRepository.existsByMoIgnoreCase(workOrderRequest.mo())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "MO already exist");
        }

        WorkOrder workOrder = workOrderMapper.fromWorkOrderRequest(workOrderRequest);
        if (workOrderRequest.buyerId() != null){
            Buyer buyer = buyerRepository.findById(workOrderRequest.buyerId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Buyer not found!")
            );
            workOrder.setBuyer(buyer);
        }

        if (workOrderRequest.colorId() != null){
            Color color = colorRepository.findById(workOrderRequest.colorId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Color not found!")
            );
            workOrder.setColor(color);
        }

        if (!workOrderRequest.sizeIds().isEmpty()){
           List<Size> sizes = sizeRepository.findByIdIn(workOrderRequest.sizeIds());
           workOrder.setSizes(sizes);
        }

        workOrder.setStatus(WorkOrderStatus.NEW);
        workOrder.setOrderFollower(user.getNameEn());
        WorkOrder savedWorkOrder = workOrderRepository.save(workOrder);

        return workOrderMapper.toWorkOrderResponse(savedWorkOrder);
    }

    @Override
    public List<WorkOrderLookupResponse> getWorkOrderLookup() {
        List<WorkOrder> workOrders = workOrderRepository.findAll();

        return workOrders.stream().map(wo -> WorkOrderLookupResponse.builder()
                .id(wo.getId())
                .mo(wo.getMo())
                .build()).toList();
    }

    @Override
    public ResponseEntity<Map<String, String>> getStyleByMo(String mo) {
        WorkOrder workOrder = workOrderRepository.findByMo(mo).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Work order not found!")
        );
        return ResponseEntity.ok(Map.of("style", workOrder.getStyle()));
    }

    @Override
    public Page<WorkOrderResponse> findAll(WorkOrderFilterRequest workOrderFilterRequest) {
        if (workOrderFilterRequest.pageNo() <= 0 || workOrderFilterRequest.pageSize() <= 0 ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page no and Page size must be greater than 0");
        }

        Specification<WorkOrder> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (workOrderFilterRequest.search() != null){
            String searchTerm = "%" + workOrderFilterRequest.search().toLowerCase() + "%";
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("mo")), searchTerm)
            );
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(workOrderFilterRequest.pageNo() - 1, workOrderFilterRequest.pageSize(), sort);
        Page<WorkOrder> workOrders = workOrderRepository.findAll(spec, pageRequest);

        return new PageImpl<>(toWorkOrderResponse(workOrders), pageRequest, workOrders.getTotalElements());
    }

    private List<WorkOrderResponse> toWorkOrderResponse(Page<WorkOrder> workOrders){

        return workOrders.stream()
                .map(w ->{
//                    List<ProcessingDetail> processingDetails = processingDetailRepository.findByWorkOrderDetails_WorkOrder_Mo(w.getMo());
                    return WorkOrderResponse.builder()
                            .id(w.getId())
                            .mo(w.getMo())
                            .style(w.getStyle())
                            .buyer(w.getBuyer().getName())
                            .qty(w.getQty())
                            .startDate(w.getStartDate())
                            .endDate(w.getEndDate())
                            .status(w.getStatus())
                            .build() ;
                })
                .toList();
    }
}

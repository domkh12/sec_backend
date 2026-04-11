package site.secmega.secapi.feature.workOrder;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.base.WorkOrderStatus;
import site.secmega.secapi.domain.Buyer;
import site.secmega.secapi.domain.Color;
import site.secmega.secapi.domain.Size;
import site.secmega.secapi.domain.WorkOrder;
import site.secmega.secapi.feature.buyer.BuyerRepository;
import site.secmega.secapi.feature.color.ColorRepository;
import site.secmega.secapi.feature.size.SizeRepository;
import site.secmega.secapi.feature.workOrder.dto.WorkOrderFilterRequest;
import site.secmega.secapi.feature.workOrder.dto.WorkOrderRequest;
import site.secmega.secapi.feature.workOrder.dto.WorkOrderResponse;
import site.secmega.secapi.feature.workOrderColor.dto.WorkOrderColorResponse;
import site.secmega.secapi.feature.workOrderColorSize.dto.WorkOrderColorSizeResponse;
import site.secmega.secapi.mapper.WorkOrderMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkOrderServiceImpl implements WorkOrderService{

    private final WorkOrderRepository workOrderRepository;
    private final WorkOrderMapper workOrderMapper;
    private final BuyerRepository buyerRepository;
    private final ColorRepository colorRepository;
    private final SizeRepository sizeRepository;

    @Override
    public WorkOrderResponse createWorkOrder(WorkOrderRequest workOrderRequest) {

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
        WorkOrder savedWorkOrder = workOrderRepository.save(workOrder);
        return workOrderMapper.toWorkOrderResponse(savedWorkOrder);
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
                .map(w -> WorkOrderResponse.builder()
                        .mo(w.getMo())
                        .buyer(w.getBuyer().getName())
                        .qty(w.getQty())
                        .startDate(w.getStartDate())
                        .endDate(w.getEndDate())
                        .status(w.getStatus())
                        .build())
                .toList();
    }
}

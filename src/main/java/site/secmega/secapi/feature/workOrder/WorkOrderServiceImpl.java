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
import site.secmega.secapi.feature.buyer.dto.BuyerLookupResponse;
import site.secmega.secapi.feature.color.ColorRepository;
import site.secmega.secapi.feature.color.dto.ColorLookupResponse;
import site.secmega.secapi.feature.outputDetail.OutputDetailRepository;
import site.secmega.secapi.feature.size.SizeRepository;
import site.secmega.secapi.feature.size.dto.SizeLookupResponse;
import site.secmega.secapi.feature.user.UserRepository;
import site.secmega.secapi.feature.workOrder.dto.*;
import site.secmega.secapi.mapper.WorkOrderMapper;
import site.secmega.secapi.util.AuthUtil;

import java.time.LocalDateTime;
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
    private final OutputDetailRepository outputDetailRepository;

    @Override
    public void deleteWorkOrder(Long id) {
        WorkOrder workOrder = workOrderRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Work Order Not found!")
        );
        workOrder.setDeletedAt(LocalDateTime.now());
        workOrderRepository.save(workOrder);
    }

    @Override
    public WorkOrderResponse updateWorkOrder(Long id, WorkOrderRequest workOrderRequest) {
        WorkOrder workOrder = workOrderRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Work Order Not found!")
        );

        if (workOrderRepository.existsByMoIgnoreCaseAndDeletedAtNullAndIdNot(workOrder.getMo(), id)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Work order already exist!");
        }

        workOrderMapper.updateWorkOrder(workOrderRequest, workOrder);

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
        WorkOrder savedWO = workOrderRepository.save(workOrder);
//        return workOrderMapper.toWorkOrderResponse(savedWO);
        return null;
    }

    @Override
    public WorkOrderStatResponse getWorkOrderStat(WorkOrderFilterRequest workOrderFilterRequest) {

        long totalMO = workOrderRepository.countFirstBy();
        long totalWorkOrderQty = workOrderRepository.sumWorkOrderQty();
        long totalOutput = outputDetailRepository.sumGoodOutputQty();

        return WorkOrderStatResponse.builder()
                .totalMO(totalMO)
                .totalWorkOrderQty(totalWorkOrderQty)
                .totalOutput(totalOutput)
                .totalBalance(totalWorkOrderQty - totalOutput)
                .build();

    }

    @Override
    public WorkOrderResponse createWorkOrder(WorkOrderRequest workOrderRequest) {
        Long userId = authUtil.loggedUserId();
        User user = userRepository.findById(userId).orElseThrow();
        if (workOrderRepository.existsByMoIgnoreCase(workOrderRequest.mo())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "MO already exist");
        }

        WorkOrder workOrder = workOrderMapper.fromWorkOrderRequest(workOrderRequest);

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

        workOrder.setStatus(WorkOrderStatus.PENDING);
        workOrder.setOrderFollower(user.getNameEn());
        WorkOrder savedWorkOrder = workOrderRepository.save(workOrder);

//        return toWorkOrderResponse(savedWorkOrder);
        return null;
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
        return ResponseEntity.ok(Map.of("style", workOrder.getStyle().getStyleNo()));
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

    private List<WorkOrderResponse> toWorkOrderResponse(Page<WorkOrder> workOrders) {
        return workOrders.stream()
                .map(w -> {

                    // Safe mapping for Color (This is where your crash is)
                    ColorLookupResponse colorResp = (w.getColor() != null) ?
                            ColorLookupResponse.builder()
                            .id(w.getColor().getId())
                            .color(w.getColor().getColor())
                            .build() : null;

                    // Safe mapping for Sizes
                    List<SizeLookupResponse> sizeResps = (w.getSizes() != null) ?
                            w.getSizes().stream().map(size -> SizeLookupResponse.builder()
                                                              .id(size.getId())
                                                              .size(size.getSize())
                                                              .build()).toList() : List.of();
                    Integer output = outputDetailRepository.sumGoodQtyByWorkOrder_Id(w.getId());

                    return WorkOrderResponse.builder()
                            .id(w.getId())
                            .mo(w.getMo())
                            .po(w.getPurchaseOrder().getPo())
                            .style(w.getStyle().getStyleNo())
                            .color(colorResp)
                            .sizes(sizeResps)
                            .qty(w.getQty())
                            .startDate(w.getStartDate())
                            .endDate(w.getEndDate())
                            .status(w.getStatus())
                            .image(w.getImage())
                            .output(output)
                            .balance(w.getQty() - output)
                            .build();
                })
                .toList();
    }
}

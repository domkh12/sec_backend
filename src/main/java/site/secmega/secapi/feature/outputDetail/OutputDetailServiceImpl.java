package site.secmega.secapi.feature.outputDetail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.domain.*;
import site.secmega.secapi.feature.buyer.dto.BuyerLookupResponse;
import site.secmega.secapi.feature.defectDetail.DefectDetailRepository;
import site.secmega.secapi.feature.defectType.DefectTypeRepository;
import site.secmega.secapi.feature.message.dto.MessageRequest;
import site.secmega.secapi.feature.outputDetail.dto.OutputDetailRequest;
import site.secmega.secapi.feature.outputDetail.dto.OutputDetailResponse;
import site.secmega.secapi.feature.outputDetail.dto.OutputFilterRequest;
import site.secmega.secapi.feature.outputDetail.dto.OutputLast48Hrs;
import site.secmega.secapi.feature.productionLine.ProductionLineRepository;
import site.secmega.secapi.feature.productionLine.dto.ProductionLineLookupResponse;
import site.secmega.secapi.feature.purchaseOrder.dto.PurchaseOrderLookupResponse;
import site.secmega.secapi.feature.size.SizeRepository;
import site.secmega.secapi.feature.size.dto.SizeLookupResponse;
import site.secmega.secapi.feature.style.dto.StyleLookupResponse;
import site.secmega.secapi.feature.time.TimeRepository;
import site.secmega.secapi.feature.time.dto.TimeResponse;
import site.secmega.secapi.feature.tv.TvDataRepository;
import site.secmega.secapi.feature.workOrder.WorkOrderRepository;
import site.secmega.secapi.mapper.OutputDetailMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutputDetailServiceImpl implements OutputDetailService{

    private final OutputDetailMapper outputDetailMapper;
    private final TimeRepository timeRepository;
    private final WorkOrderRepository workOrderRepository;
    private final OutputDetailRepository outputDetailRepository;
    private final ProductionLineRepository productionLineRepository;
    private final SizeRepository sizeRepository;
    private final TvDataRepository tvDataRepository;
    private final SimpMessagingTemplate messagingTemplate;
    private final DefectDetailRepository defectDetailRepository;
    private final DefectTypeRepository defectTypeRepository;

    @Override
    public List<OutputLast48Hrs> outputLast48Hrs() {
        return outputDetailRepository.outputLast48Hrs().stream()
                .map(row -> new OutputLast48Hrs(
                        ((Number) row[0]).intValue(),
                        ((Number) row[1]).intValue()
                ))
                .toList();
    }

    @Override
    public void updateQty(Long id, Integer qty) {
        OutputDetail outputDetail = outputDetailRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Output not found"));
        outputDetail.setGoodQty(qty);
        outputDetailRepository.save(outputDetail);

         updateTvDataForSewing(
            outputDetail.getFromLine().getId(),
            outputDetail.getOutputDate()
        );
    }

    @Override
    public Page<OutputDetailResponse> findAll(OutputFilterRequest outputFilterRequest) {

        if (outputFilterRequest.pageNo() <= 0 || outputFilterRequest.pageSize() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Page no and Page size must be greater than 0");
        }

        Specification<OutputDetail> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (outputFilterRequest.search() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.or(
                            cb.like(cb.lower(root.get("workOrder").get("mo")), "%" + outputFilterRequest.search().toLowerCase() + "%"),
                            cb.like(cb.lower(root.get("workOrder").get("purchaseOrder").get("po")), "%" + outputFilterRequest.search().toLowerCase() + "%"),
                            cb.like(cb.lower(root.get("workOrder").get("purchaseOrder").get("style").get("styleNo")), "%" + outputFilterRequest.search().toLowerCase() + "%")
                    )
            );
        }
        
        if(outputFilterRequest.buyerId() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("workOrder").get("purchaseOrder").get("buyer").get("id"), outputFilterRequest.buyerId())
            );
        }

        if (outputFilterRequest.reportDate() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("outputDate"), outputFilterRequest.reportDate())
            );
        }

        if (outputFilterRequest.lineId() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("fromLine").get("id"), outputFilterRequest.lineId())
            );
        }

        if (outputFilterRequest.sizeId() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.equal(root.get("size").get("id"), outputFilterRequest.sizeId())
            );
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(outputFilterRequest.pageNo() - 1, outputFilterRequest.pageSize(), sort);
        Page<OutputDetail> outputDetails = outputDetailRepository.findAll(spec, pageRequest);

        return new PageImpl<>(
            outputDetails.stream().map(
                detail -> OutputDetailResponse.builder()
                        .id(detail.getId())
                        .reportDate(detail.getCreatedAt())
                        .qty(detail.getGoodQty())
                        .size(SizeLookupResponse.builder()
                                .id(detail.getSize().getId())
                                .size(detail.getSize().getSize())
                                .build())
                        .mo(detail.getWorkOrder().getMo())
                        .outputDate(detail.getOutputDate())
                        .time(TimeResponse.builder()
                                .id(detail.getId())
                                .name(detail.getTime().getName())
                                .build())
                        .line(ProductionLineLookupResponse.builder()
                                .id(detail.getFromLine().getId())
                                .line(detail.getFromLine().getLine())
                                .build())
                        .purchaseOrder(PurchaseOrderLookupResponse.builder()
                                .id(detail.getWorkOrder().getPurchaseOrder().getId())
                                .po(detail.getWorkOrder().getPurchaseOrder().getPo())
                                .build())
                        .style(StyleLookupResponse.builder()
                                .id(detail.getWorkOrder().getPurchaseOrder().getStyle().getId())
                                .styleNo(detail.getWorkOrder().getPurchaseOrder().getStyle().getStyleNo())
                                .build())
                        .buyer(BuyerLookupResponse.builder()
                                .id(detail.getWorkOrder().getPurchaseOrder().getBuyer().getId())
                                .name(detail.getWorkOrder().getPurchaseOrder().getBuyer().getName())
                                .build())

                        .build()
            ).toList(),
            pageRequest,
            outputDetails.getTotalElements()
        );
    }

    @Override
    public void delete(Long id) {
        OutputDetail outputDetail = outputDetailRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Output not found")
        );

         Long lineId = outputDetail.getFromLine().getId();
    LocalDate outputDate = outputDetail.getOutputDate();

        outputDetail.setDeletedAt(LocalDateTime.now());
        outputDetailRepository.save(outputDetail);

        updateTvDataForSewing(lineId, outputDate);
    }


    @Override
    public List<OutputDetailResponse> createOutputDetail(List<OutputDetailRequest> outputDetailRequest) {

        outputDetailRequest.forEach(od -> {
            Time time = timeRepository.findById(od.timeId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Time not found")
            );
            OutputDetail outputDetail = outputDetailMapper.fromOutputDetailRequest(od);

            outputDetail.setTime(time);

            WorkOrder workOrder = workOrderRepository.findByMo(od.mo()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "MO not found")
            );
            outputDetail.setWorkOrder(workOrder);

            ProductionLine fromLine = productionLineRepository.findById(od.fromLineId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "From Line not found!")
            );
            outputDetail.setFromLine(fromLine);

            if (od.toLineId() != null) {
                ProductionLine toLine = productionLineRepository.findById(od.toLineId()).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "To Line not found!")
                );
                outputDetail.setToLine(toLine);
            }

            if(od.sizeId() != null){
                Size size = sizeRepository.findById(od.sizeId()).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Size not found")
                );
                outputDetail.setSize(size);
                outputDetailRepository.save(outputDetail);   
            }
            

            if (od.defectTypeId() != null){
                DefectType df = defectTypeRepository.findByIdAndDeletedAtNull(od.defectTypeId()).orElseThrow(
                        () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Defect type not found")
                );
                DefectDetail defectDetail = new DefectDetail();
                defectDetail.setDefectType(df);
                defectDetail.setTime(time);
                defectDetail.setDefectQty(od.defectQty());
                defectDetail.setProductionLine(fromLine);
                defectDetail.setWorkOrder(workOrder);
                defectDetail.setDefectDate(od.outputDate());
                defectDetailRepository.save(defectDetail);
            }

        });

        // logic update qty in TV
         
        updateTvDataForSewing(
        outputDetailRequest.get(0).fromLineId(),
        outputDetailRequest.get(0).outputDate()
        );

        return null;
        

    }

    private void updateTvDataForSewing(Long lineId, LocalDate outputDate) {

//        ProductionLine productionLine = productionLineRepository.findById(lineId)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Line not found!"));
//
//        if (productionLine.getDepartment() == null
//                || productionLine.getDepartment().getProcessNo() == null
//                || productionLine.getDepartment().getProcessNo() != 2) {
//            return;
//        }
//
//        String lineName = productionLine.getLine();
//        Integer processNo = productionLine.getDepartment().getProcessNo();
//
//        TvData tvData = tvDataRepository.findByDateAndTv_Name(outputDate.toString(), lineName)
//                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "TV Data not found"));
//
//        for (Time time : timeRepository.findAll()) {
//            Integer qty = outputDetailRepository.totalOutputSewingBetweenDatesByTimeAndLine(
//                    outputDate,
//                    outputDate,
//                    processNo,
//                    time.getId(),
//                    lineId
//            );
//
//            switch (time.getName()) {
//                case "07:00-08:00" -> tvData.setH8(qty);
//                case "08:00-09:00" -> tvData.setH9(qty);
//                case "09:00-10:00" -> tvData.setH10(qty);
//                case "10:00-11:00" -> tvData.setH11(qty);
//                case "12:00-13:00" -> tvData.setH13(qty);
//                case "13:00-14:00" -> tvData.setH14(qty);
//                case "14:00-15:00" -> tvData.setH15(qty);
//                case "15:00-16:00" -> tvData.setH16(qty);
//                case "16:00-17:00" -> tvData.setH17(qty);
//                case "17:00-18:00" -> tvData.setH18(qty);
//            }
//        }
//
//        tvDataRepository.save(tvData);
//
//        messagingTemplate.convertAndSend(
//                "/topic/messages/tv-data-update",
//                MessageRequest.builder()
//                        .message("update")
//                        .isUpdate(true)
//                        .build()
//        );
    }

}

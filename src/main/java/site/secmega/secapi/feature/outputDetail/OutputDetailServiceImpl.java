package site.secmega.secapi.feature.outputDetail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.domain.*;
import site.secmega.secapi.feature.outputDetail.dto.OutputDetailRequest;
import site.secmega.secapi.feature.outputDetail.dto.OutputDetailResponse;
import site.secmega.secapi.feature.outputDetail.dto.OutputFilterRequest;
import site.secmega.secapi.feature.productionLine.ProductionLineRepository;
import site.secmega.secapi.feature.productionLine.dto.ProductionLineLookupResponse;
import site.secmega.secapi.feature.size.SizeRepository;
import site.secmega.secapi.feature.size.dto.SizeLookupResponse;
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

    @Override
    public void updateQty(Long id, Integer qty) {
        OutputDetail outputDetail = outputDetailRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Output not found"));
        outputDetail.setGoodQty(qty);
        outputDetailRepository.save(outputDetail);
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
                            cb.like(cb.lower(root.get("workOrder").get("mo")), "%" + outputFilterRequest.search().toLowerCase() + "%")
                    )
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

        outputDetail.setDeletedAt(LocalDateTime.now());
        outputDetailRepository.save(outputDetail);
    }


    @Override
    public List<OutputDetailResponse> createOutputDetail(List<OutputDetailRequest> outputDetailRequest) {

        outputDetailRequest.forEach(od -> {
            OutputDetail outputDetail = outputDetailMapper.fromOutputDetailRequest(od);
            Time time = timeRepository.findById(od.timeId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Time not found")
            );
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

            Size size = sizeRepository.findById(od.sizeId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Size not found")
            );
            outputDetail.setSize(size);
            outputDetailRepository.save(outputDetail);
        });

        // logic update qty in TV
        Long lineId = outputDetailRequest.get(0).fromLineId();

        ProductionLine productionLine = productionLineRepository.findById(lineId).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Line not found!")
         );
        String lineName = productionLine.getLine();

        LocalDate outputDate = outputDetailRequest.get(0).outputDate();
        Integer processNo = productionLine.getDepartment().getProcessNo();
        TvData tvData = tvDataRepository.findByDateAndTv_Name(outputDate.toString(), lineName).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "TV Data not found")
        );

        List<Time> times = timeRepository.findAll();

        times.forEach(time -> {
            Integer qty = outputDetailRepository.totalOutputSewingBetweenDatesByTime(outputDate,outputDate,processNo,time.getId());
            // set qty to h8, h9, h10, h11, h13, h14, h15, h16, h17, h18
            // but name of my time is 7:00-8:00
            if (time.getName().equals("07:00-08:00")) {
                tvData.setH8(qty);
            } else if (time.getName().equals("08:00-09:00")) {
                tvData.setH9(qty);
            } else if (time.getName().equals("09:00-10:00")) {
                tvData.setH10(qty);
            } else if (time.getName().equals("10:00-11:00")) {
                tvData.setH11(qty);
            } else if (time.getName().equals("12:00-13:00")) {
                tvData.setH13(qty);
            } else if (time.getName().equals("13:00-14:00")) {
                tvData.setH14(qty);
            } else if (time.getName().equals("14:00-15:00")) {
                tvData.setH15(qty);
            } else if (time.getName().equals("15:00-16:00")) {
                tvData.setH16(qty);
            } else if (time.getName().equals("16:00-17:00")) {
                tvData.setH17(qty);
            } else if (time.getName().equals("17:00-18:00")) {
                tvData.setH18(qty);
            }
            tvDataRepository.save(tvData);
        });

        log.info("Line name: {}", lineName);

        return null;

    }

}

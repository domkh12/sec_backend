package site.secmega.secapi.feature.outputDetail;

import lombok.RequiredArgsConstructor;
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
import site.secmega.secapi.feature.workOrder.WorkOrderRepository;
import site.secmega.secapi.mapper.OutputDetailMapper;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OutputDetailServiceImpl implements OutputDetailService{

    private final OutputDetailMapper outputDetailMapper;
    private final TimeRepository timeRepository;
    private final WorkOrderRepository workOrderRepository;
    private final OutputDetailRepository outputDetailRepository;
    private final ProductionLineRepository productionLineRepository;
    private final SizeRepository sizeRepository;

    @Override
    public Page<OutputDetailResponse> findAll(OutputFilterRequest outputFilterRequest) {

        Specification<OutputDetail> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (outputFilterRequest.search() != null) {
            spec = spec.and((root, query, cb) ->
                    cb.or(
                            cb.like(cb.lower(root.get("workOrder").get("mo")), "%" + outputFilterRequest.search().toLowerCase() + "%")
                    )
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
        return null;
    }

}

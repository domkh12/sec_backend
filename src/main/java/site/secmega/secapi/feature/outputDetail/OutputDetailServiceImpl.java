package site.secmega.secapi.feature.outputDetail;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.domain.*;
import site.secmega.secapi.feature.outputDetail.dto.OutputDetailRequest;
import site.secmega.secapi.feature.outputDetail.dto.OutputDetailResponse;
import site.secmega.secapi.feature.productionLine.ProductionLineRepository;
import site.secmega.secapi.feature.size.SizeRepository;
import site.secmega.secapi.feature.time.TimeRepository;
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

            ProductionLine toLine = productionLineRepository.findById(od.toLineId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "To Line not found!")
            );
            outputDetail.setToLine(toLine);

            Size size = sizeRepository.findById(od.sizeId()).orElseThrow(
                    () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Size not found")
            );
            outputDetail.setSize(size);

            outputDetail.setOutputDate(LocalDate.now());
            outputDetailRepository.save(outputDetail);
        });
        return null;
    }

}

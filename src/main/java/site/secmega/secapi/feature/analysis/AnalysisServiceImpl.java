package site.secmega.secapi.feature.analysis;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import site.secmega.secapi.feature.analysis.dto.AnalysisOutputResponse;
import site.secmega.secapi.feature.analysis.dto.MoResponse;
import site.secmega.secapi.feature.outputDetail.OutputDetailRepository;
import site.secmega.secapi.feature.workOrder.WorkOrderRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalysisServiceImpl implements AnalysisService{

    private final OutputDetailRepository outputDetailRepository;
    private final WorkOrderRepository workOrderRepository;

    @Override
    public AnalysisOutputResponse getAnalysisOutputToday() {
        LocalDate today = LocalDate.now();
        Integer totalInput = outputDetailRepository.totalInputByDate(today, 1);
        Integer totalOutput = outputDetailRepository.totalOutputSewingByDate(today, 2);
        List<MoResponse> moResponses = workOrderRepository.findByIsActive(true).stream().map(
                wo -> MoResponse.builder()
                        .mo(wo.getMo())
                        .build()
        ).toList();
        return AnalysisOutputResponse.builder()
                .totalInput(totalInput)
                .totalOutput(totalOutput)
                .mo(moResponses)
                .build();
    }
}

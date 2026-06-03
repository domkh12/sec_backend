package site.secmega.secapi.feature.analysis;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import site.secmega.secapi.feature.analysis.dto.AnalysisOutputResponse;
import site.secmega.secapi.feature.analysis.dto.MoResponse;
import site.secmega.secapi.feature.analysis.dto.SizeOutput;
import site.secmega.secapi.feature.outputDetail.OutputDetailRepository;
import site.secmega.secapi.feature.style.StyleRepository;
import site.secmega.secapi.feature.workOrder.WorkOrderRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AnalysisServiceImpl implements AnalysisService{

    private final OutputDetailRepository outputDetailRepository;
    private final WorkOrderRepository workOrderRepository;
    private final StyleRepository styleRepository;

    @Override
    public AnalysisOutputResponse getAnalysisOutputToday() {
        LocalDate today = LocalDate.now();
        Integer totalInput = outputDetailRepository.totalInputByDate(today, 1);
        Integer totalOutput = outputDetailRepository.totalOutputSewingByDate(today, 2);
        Integer totalStyleActive = styleRepository.countStylesWithOutputDetailsToday(today);
        List<MoResponse> moResponses = workOrderRepository.findByIsActive(true).stream().map(
                wo -> MoResponse.builder()
                        .mo(wo.getMo())
                        .buyer(wo.getPurchaseOrder().getBuyer().getName())
                        .outputQty(outputDetailRepository.totalOutputTodayByMO(wo.getMo(), today, 2))
                        .inputQty(outputDetailRepository.totalOutputTodayByMO(wo.getMo(), today, 1))
                        .sizeOutputs(wo.getSizes().stream().map(size -> SizeOutput.builder()
                                .size(size.getSize())
                                .inputQty(outputDetailRepository.totalOutputTodayByMOAndSize(today, wo.getMo(), 1, size.getId()))
                                .outputQty(outputDetailRepository.totalOutputTodayByMOAndSize(today, wo.getMo(), 2, size.getId()))
                                .build()).toList())
                        .build()
        ).toList();
        return AnalysisOutputResponse.builder()
                .totalInput(totalInput)
                .totalOutput(totalOutput)
                .totalStyleActive(totalStyleActive)
                .mo(moResponses)
                .build();
    }
}

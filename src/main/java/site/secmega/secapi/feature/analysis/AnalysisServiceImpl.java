package site.secmega.secapi.feature.analysis;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import site.secmega.secapi.domain.WorkOrder;
import site.secmega.secapi.feature.analysis.dto.*;
import site.secmega.secapi.feature.buyer.BuyerRepository;
import site.secmega.secapi.feature.color.dto.ColorLookupResponse;
import site.secmega.secapi.feature.outputDetail.OutputDetailRepository;
import site.secmega.secapi.feature.productionLine.ProductionLineRepository;
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
    private final BuyerRepository buyerRepository;
    private final ProductionLineRepository productionLineRepository;

    @Override
    public AnalysisOutputResponse getAnalysisOutputToday() {
        LocalDate today = LocalDate.now();
        Integer totalInputToday = outputDetailRepository.totalInputByDate(today, 1);
        Integer totalOutputToday = outputDetailRepository.totalOutputSewingByDate(today, 2);
        Integer totalStyleActive = styleRepository.countStylesWithOutputDetailsToday(today);
        Integer totalWorkOrderQty = workOrderRepository.sumByIsActiveTrue();
        Integer totalOutput = outputDetailRepository.totalOutputQty(2);
        Integer totalBalance = totalWorkOrderQty - totalOutput;
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
                        .color(ColorLookupResponse.builder()
                                .id(wo.getColor().getId())
                                .color(wo.getColor().getColor())
                                .build())
                        .build()
        ).toList();
        List<BuyerAnalysisResponse> buyerAnalysisResponses = buyerRepository.findByDeletedAtNull().stream().map(buyer ->
                BuyerAnalysisResponse.builder()
                        .id(buyer.getId())
                        .name(buyer.getName())
                        .mos((int) buyer.getPurchaseOrders().stream().flatMap(po -> po.getWorkOrders().stream()).filter(wo -> wo.getIsActive()).count())
                        .outputQty(buyer.getPurchaseOrders().stream().flatMap(po -> po.getWorkOrders().stream()).filter(wo -> wo.getIsActive()).mapToInt(wo -> outputDetailRepository.totalOutputTodayByMO(wo.getMo(), today, 2)).sum())
                        .inputQty(buyer.getPurchaseOrders().stream().flatMap(po -> po.getWorkOrders().stream()).filter(wo -> wo.getIsActive()).mapToInt(wo -> outputDetailRepository.totalOutputTodayByMO(wo.getMo(), today, 1)).sum())
                        .build()
                ).toList();
        List<LineDataResponse> lineDataResponses =
                productionLineRepository.findByDepartment_ProcessNo(2)
                        .stream()
                        .map(line -> {

                            List<WorkOrder> activeWos = line.getWorkOrders()
                                    .stream()
                                    .filter(WorkOrder::getIsActive)
                                    .toList();

                            String buyer = activeWos.isEmpty()
                                    ? null
                                    : activeWos.getFirst()
                                    .getPurchaseOrder()
                                    .getBuyer()
                                    .getName();

                            return LineDataResponse.builder()
                                    .x(line.getLine())
                                    .buyer(buyer)
                                    .y(outputDetailRepository.sumOutputByLine(today, line.getId()))
                                    .mos(activeWos.stream()
                                            .map(wo -> MoResponse.builder()
                                                    .mo(wo.getMo())
                                                    .outputQty(outputDetailRepository.sumOutputTodayByMOAndLine(
                                                            wo.getMo(),
                                                            today,
                                                            line.getId()))
                                                    .inputQty(outputDetailRepository.sumOutputTodayByMOAndLine(
                                                            wo.getMo(),
                                                            today,
                                                            line.getId()))
                                                    .build())
                                            .toList())
                                    .build();
                        })
                        .toList();
        return AnalysisOutputResponse.builder()
                .totalInput(totalInputToday)
                .totalOutput(totalOutputToday)
                .totalStyleActive(totalStyleActive)
                .totalBalance(totalBalance)
                .mo(moResponses)
                .buyers(buyerAnalysisResponses)
                .lineData(lineDataResponses)
                .build();
    }
}

package site.secmega.secapi.feature.analysis;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import site.secmega.secapi.domain.WorkOrder;
import site.secmega.secapi.feature.analysis.dto.*;
import site.secmega.secapi.feature.buyer.BuyerRepository;
import site.secmega.secapi.feature.color.dto.ColorLookupResponse;
import site.secmega.secapi.feature.outputDetail.OutputDetailRepository;
import site.secmega.secapi.feature.analysis.dto.OutputLast48Hrs;
import site.secmega.secapi.feature.productionLine.ProductionLineRepository;
import site.secmega.secapi.feature.style.StyleRepository;
import site.secmega.secapi.feature.workOrder.WorkOrderRepository;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalysisServiceImpl implements AnalysisService{

    private final OutputDetailRepository outputDetailRepository;
    private final WorkOrderRepository workOrderRepository;
    private final StyleRepository styleRepository;
    private final BuyerRepository buyerRepository;
    private final ProductionLineRepository productionLineRepository;

    @Override
    public AnalysisOutputResponse getAnalysis(LocalDate dateFrom, LocalDate dateTo) {
        long periodDays = ChronoUnit.DAYS.between(dateFrom, dateTo) + 1;
        LocalDate compareDateFrom = dateFrom.minusDays(periodDays);
        LocalDate compareDateTo = dateFrom.minusDays(1);

        // Get data as Object arrays
        List<Object[]> currentData = outputDetailRepository.getDailySummaryBetweenDates(dateFrom, dateTo);
        List<Object[]> prevData = outputDetailRepository.getDailySummaryBetweenDates(compareDateFrom, compareDateTo);

        // Process current data
        int totalInput = 0;
        int totalOutput = 0;
        Map<LocalDate, LineChartDataResponse> dataMap = new LinkedHashMap<>();

        for (Object[] row : currentData) {
            LocalDate date = (LocalDate) row[0];
            Integer input = ((Number) row[1]).intValue();
            Integer output = ((Number) row[2]).intValue();

            totalInput += input;
            totalOutput += output;

            dataMap.put(date, LineChartDataResponse.builder()
                    .date(date)
                    .input(input)
                    .output(output)
                    .build());
        }

        // Process previous data
        int prevTotalInput = 0;
        int prevTotalOutput = 0;
        for (Object[] row : prevData) {
            prevTotalInput += ((Number) row[1]).intValue();
            prevTotalOutput += ((Number) row[2]).intValue();
        }

        // Build final response
        List<LineChartDataResponse> data = dateFrom.datesUntil(dateTo.plusDays(1))
                .map(date -> dataMap.getOrDefault(date,
                        LineChartDataResponse.builder()
                                .date(date)
                                .input(0)
                                .output(0)
                                .build()))
                .collect(Collectors.toList());

        return AnalysisOutputResponse.builder()
                .totalInput(totalInput)
                .totalOutput(totalOutput)
                .totalInputComparison(buildComparison(totalInput, prevTotalInput))
                .totalOutputComparison(buildComparison(totalOutput, prevTotalOutput))
                .data(data)
                .build();
    }

    @Override
    public AnalysisOutputTodayResponse getAnalysisOutputToday() {
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
        List<BuyerAnalysisResponse> buyerAnalysisResponses = buyerRepository.findByDeletedAtNullAndPurchaseOrders_WorkOrders_IsActiveTrue().stream().map(buyer ->
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
        return AnalysisOutputTodayResponse.builder()
                .totalInput(totalInputToday)
                .totalOutput(totalOutputToday)
                .totalStyleActive(totalStyleActive)
                .totalBalance(totalBalance)
                .mo(moResponses)
                .buyers(buyerAnalysisResponses)
                .lineData(lineDataResponses)
                .build();
    }

    @Override
    public List<OutputLast48Hrs> outputLast48Hrs() {
        return outputDetailRepository.outputLast48Hrs().stream()
                .map(row -> new OutputLast48Hrs(
                        ((Number) row[0]).intValue(),
                        ((Number) row[1]).intValue()
                ))
                .toList();
    }


    private ComparisonResponse buildComparison(Integer current, Integer previous) {
        int curr = current != null ? current : 0;
        int prev = previous != null ? previous : 0;
        int diff = curr - prev;

        double changePercent = (prev == 0)
                ? (curr > 0 ? 100.0 : 0.0)
                : ((double) diff / prev) * 100;

        return ComparisonResponse.builder()
                .current(curr)
                .previous(prev)
                .diff(diff)
                .changePercent(Math.round(changePercent * 100.0) / 100.0) // 2 decimal
                .trend(diff > 0 ? "UP" : diff < 0 ? "DOWN" : "FLAT")
                .build();
    }
}

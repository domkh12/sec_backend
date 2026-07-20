package site.secmega.secapi.feature.analysis;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import site.secmega.secapi.domain.DefectType;
import site.secmega.secapi.domain.ProductionLine;
import site.secmega.secapi.domain.Time;
import site.secmega.secapi.domain.WorkOrder;
import site.secmega.secapi.feature.analysis.dto.*;
import site.secmega.secapi.feature.buyer.BuyerRepository;
import site.secmega.secapi.feature.color.dto.ColorLookupResponse;
import site.secmega.secapi.feature.defectDetail.DefectDetailRepository;
import site.secmega.secapi.feature.defectType.DefectTypeRepository;
import site.secmega.secapi.feature.defectType.dto.DefectTypeWithQtyResponse;
import site.secmega.secapi.feature.outputDetail.OutputDetailRepository;
import site.secmega.secapi.feature.analysis.dto.OutputLast48Hrs;
import site.secmega.secapi.feature.productionLine.ProductionLineRepository;
import site.secmega.secapi.feature.style.StyleRepository;
import site.secmega.secapi.feature.time.TimeRepository;
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
    private final DefectDetailRepository defectDetailRepository;
    private final DefectTypeRepository defectTypeRepository;
    private final TimeRepository timeRepository;

    @Override
    public AnalysisDefectResponse defectToday() {
        LocalDate today = LocalDate.now();
        List<ProductionLine> productionLines = productionLineRepository.findByDeletedAtNullAndDepartment_ProcessNo(2);
        List<LineDefectResponse> lineDefectResponses = productionLines.stream().map(pl -> {
            List<WorkOrder> mo = workOrderRepository.findByDeletedAtNullAndIsActiveTrueAndProductionLines_Id(pl.getId());

            List<MosResponse> mosResponses = mo.stream().map(
                    mos -> {
                        List<DefectType> defectTypes = defectTypeRepository.findByDeletedAtNullAndDefectDetails_WorkOrder_IsActiveTrueAndDefectDetails_WorkOrder_Mo(mos.getMo());
                        List<DefectTypeWithQtyResponse> defectTypeWithQtyResponses = defectTypes.stream().map(
                                defectType -> {
                                    Integer defectQty = defectDetailRepository.totalDefectByMoAndDefectTypeId(today, mos.getMo(), pl.getId(), defectType.getId());
                                    return DefectTypeWithQtyResponse.builder()
                                            .id(defectType.getId())
                                            .type(defectType.getName())
                                            .qty(defectQty)
                                            .build();
                                }
                        ).toList();
                        return MosResponse.builder()
                                .mo(mos.getMo())
                                .buyer(mos.getPurchaseOrder().getBuyer().getName())
                                .style(mos.getPurchaseOrder().getStyle().getStyleNo())
                                .output(outputDetailRepository.totalOutputTodayByMOAndLineId(mos.getMo(), today, pl.getId()))
                                .defect(defectDetailRepository.totalDefectByMO(today, mos.getMo(), pl.getId()))
                                .defectTypes(defectTypeWithQtyResponses)
                                .build();
                    }
            ).toList();

            return LineDefectResponse.builder()
                    .line(pl.getLine())
                    .mos(mosResponses)
                    .build();
        }).toList();

        List<Time> times = timeRepository.findByDeletedAtNull();
        List<HourlyDefectResponse> hourlyDefectResponses = times.stream().map(time -> {
            return HourlyDefectResponse.builder()
                    .hour(time.getName())
                    .output(outputDetailRepository.totalOutputByTimeId(today, time.getId()))
                    .defect(defectDetailRepository.totalDefectQtyTodayByTimeId(today, time.getId()))
                    .build();
        }).toList();
        return AnalysisDefectResponse.builder()
                .updatedAt(today.toString())
                .targetDefectRate(3.0)
                .lines(lineDefectResponses)
                .hourlyTrend(hourlyDefectResponses)
                .build();
    }

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
                        .buyer(wo.getPurchaseOrder().getBuyer() != null ? wo.getPurchaseOrder().getBuyer().getName() : null)
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
                buyer != null ? (
                BuyerAnalysisResponse.builder()
                        .id(buyer.getId())
                        .name(buyer.getName())
                        .mos((int) buyer.getPurchaseOrders().stream().flatMap(po -> po.getWorkOrders().stream()).filter(wo -> wo.getIsActive()).count())
                        .outputQty(buyer.getPurchaseOrders().stream().flatMap(po -> po.getWorkOrders().stream()).filter(wo -> wo.getIsActive()).mapToInt(wo -> outputDetailRepository.totalOutputTodayByMO(wo.getMo(), today, 2)).sum())
                        .inputQty(buyer.getPurchaseOrders().stream().flatMap(po -> po.getWorkOrders().stream()).filter(wo -> wo.getIsActive()).mapToInt(wo -> outputDetailRepository.totalOutputTodayByMO(wo.getMo(), today, 1)).sum())
                        .build()): null
                ).toList();
        List<LineDataResponse> lineDataResponses =
                productionLineRepository.findByDepartment_ProcessNo(2)
                        .stream()
                        .map(line -> {

                            List<WorkOrder> activeWos = line.getWorkOrders()
                                    .stream()
                                    .filter(WorkOrder::getIsActive)
                                    .toList();

                            String buyer = null;

                            if (!activeWos.isEmpty()) {
                                var purchaseOrder = activeWos.getFirst().getPurchaseOrder();

                                if (purchaseOrder != null && purchaseOrder.getBuyer() != null) {
                                    buyer = purchaseOrder.getBuyer().getName();
                                }
                            }

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
                .totalBalance(totalBalance < 0 ? 0 : totalBalance)
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

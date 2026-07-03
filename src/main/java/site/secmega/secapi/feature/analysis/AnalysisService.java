package site.secmega.secapi.feature.analysis;

import site.secmega.secapi.feature.analysis.dto.AnalysisOutputResponse;
import site.secmega.secapi.feature.analysis.dto.AnalysisOutputTodayResponse;
import site.secmega.secapi.feature.analysis.dto.OutputLast48Hrs;

import java.time.LocalDate;
import java.util.List;

public interface AnalysisService {
    AnalysisOutputResponse getAnalysis(LocalDate dateFrom, LocalDate dateTo);

    AnalysisOutputTodayResponse getAnalysisOutputToday();

    List<OutputLast48Hrs> outputLast48Hrs();
}

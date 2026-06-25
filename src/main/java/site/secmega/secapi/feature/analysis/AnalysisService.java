package site.secmega.secapi.feature.analysis;

import site.secmega.secapi.feature.analysis.dto.AnalysisOutputResponse;
import site.secmega.secapi.feature.analysis.dto.AnalysisOutputTodayResponse;

import java.time.LocalDate;

public interface AnalysisService {
    AnalysisOutputResponse getAnalysis(LocalDate dateFrom, LocalDate dateTo);

    AnalysisOutputTodayResponse getAnalysisOutputToday();
}

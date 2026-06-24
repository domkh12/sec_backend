package site.secmega.secapi.feature.analysis;

import site.secmega.secapi.feature.analysis.dto.AnalysisOutputResponse;

import java.time.LocalDate;

public interface AnalysisService {
    AnalysisOutputResponse getAnalysisOutputToday();

    AnalysisOutputResponse getAnalysis(LocalDate dateFrom, LocalDate dateTo);
}

package site.secmega.secapi.feature.analysis;

import site.secmega.secapi.feature.analysis.dto.*;

import java.time.LocalDate;
import java.util.List;

public interface AnalysisService {
    AnalysisInputTodayResponse getAnalysisInputToday();

    AnalysisOutputResponse getAnalysis(LocalDate dateFrom, LocalDate dateTo);

    AnalysisOutputTodayResponse getAnalysisOutputToday();

    List<OutputLast48Hrs> outputLast48Hrs();

    AnalysisDefectResponse defectToday();
}

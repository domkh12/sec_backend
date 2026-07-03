package site.secmega.secapi.feature.analysis;

import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import site.secmega.secapi.feature.analysis.dto.AnalysisOutputResponse;
import site.secmega.secapi.feature.analysis.dto.AnalysisOutputTodayResponse;
import site.secmega.secapi.feature.analysis.dto.OutputLast48Hrs;

@RestController
@RequestMapping("/api/v1/analysis")
@RequiredArgsConstructor
public class AnalysisController {

    private final AnalysisService analysisService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/last48hrs")
    @ResponseStatus(HttpStatus.OK)
    List<OutputLast48Hrs> outputLast48Hrs(){
        return analysisService.outputLast48Hrs();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    AnalysisOutputResponse getAnalysis(
        @RequestParam(required = false) LocalDate dateFrom,
        @RequestParam(required = false) LocalDate dateTo
    ) {
        return analysisService.getAnalysis(dateFrom, dateTo);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/output-today")
    @ResponseStatus(HttpStatus.OK)
    AnalysisOutputTodayResponse getAnalysisOutputToday() {
        return analysisService.getAnalysisOutputToday();
    }

}

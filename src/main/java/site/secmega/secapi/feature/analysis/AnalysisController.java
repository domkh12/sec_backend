package site.secmega.secapi.feature.analysis;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import site.secmega.secapi.feature.analysis.dto.AnalysisOutputResponse;

@RestController
@RequestMapping("/api/v1/analysis")
@RequiredArgsConstructor
public class AnalysisController {

    private final AnalysisService analysisService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/output")
    @ResponseStatus(HttpStatus.OK)
    AnalysisOutputResponse getAnalysisOutput(){
        return analysisService.getAnalysisOutput();
    }

}

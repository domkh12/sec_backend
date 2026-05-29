package site.secmega.secapi.feature.analysis;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import site.secmega.secapi.feature.analysis.dto.AnalysisOutputResponse;
import site.secmega.secapi.feature.outputDetail.OutputDetailRepository;

@Service
@RequiredArgsConstructor
public class AnalysisServiceImpl implements AnalysisService{

    private final OutputDetailRepository outputDetailRepository;

    @Override
    public AnalysisOutputResponse getAnalysisOutput() {
        return null;
    }
}

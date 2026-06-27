package site.secmega.secapi.feature.outputDetail;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import site.secmega.secapi.feature.outputDetail.dto.OutputDetailRequest;
import site.secmega.secapi.feature.outputDetail.dto.OutputDetailResponse;
import site.secmega.secapi.feature.outputDetail.dto.OutputFilterRequest;

import java.util.List;

public interface OutputDetailService {

    List<OutputDetailResponse> createOutputDetail(@Valid List<OutputDetailRequest> outputDetailRequest);

    Page<OutputDetailResponse> findAll(OutputFilterRequest outputFilterRequest);

    void delete(Long id);
}

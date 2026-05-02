package site.secmega.secapi.feature.outputDetail;

import jakarta.validation.Valid;
import site.secmega.secapi.feature.outputDetail.dto.OutputDetailRequest;
import site.secmega.secapi.feature.outputDetail.dto.OutputDetailResponse;

import java.util.List;

public interface OutputDetailService {

    List<OutputDetailResponse> createOutputDetail(@Valid List<OutputDetailRequest> outputDetailRequest);

}

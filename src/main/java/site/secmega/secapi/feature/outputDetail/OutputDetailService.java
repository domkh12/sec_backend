package site.secmega.secapi.feature.outputDetail;

import jakarta.validation.Valid;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import site.secmega.secapi.feature.outputDetail.dto.OutputDetailRequest;
import site.secmega.secapi.feature.outputDetail.dto.OutputDetailResponse;
import site.secmega.secapi.feature.outputDetail.dto.OutputFilterRequest;
import site.secmega.secapi.feature.outputDetail.dto.OutputLast48Hrs;

import java.io.IOException;
import java.util.List;

public interface OutputDetailService {

    ResponseEntity<InputStreamResource> getReportOutputDetail(OutputFilterRequest outputFilterRequest) throws IOException;

    List<OutputLast48Hrs> outputLast48Hrs();

    void updateQty(Long id, Integer qty);

    List<OutputDetailResponse> createOutputDetail(@Valid List<OutputDetailRequest> outputDetailRequest);

    Page<OutputDetailResponse> findAll(OutputFilterRequest outputFilterRequest);

    void delete(Long id);
}

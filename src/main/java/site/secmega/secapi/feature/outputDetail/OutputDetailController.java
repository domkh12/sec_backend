package site.secmega.secapi.feature.outputDetail;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.secmega.secapi.feature.outputDetail.dto.OutputDetailRequest;
import site.secmega.secapi.feature.outputDetail.dto.OutputDetailResponse;
import site.secmega.secapi.feature.outputDetail.dto.QRScanRequest;

import java.util.List;

@RestController
@RequestMapping("/api/v1/output-details")
@RequiredArgsConstructor
public class OutputDetailController {

    private final OutputDetailService outputDetailService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    List<OutputDetailResponse> createOutputDetail(@RequestBody @Valid List<OutputDetailRequest> outputDetailRequest){
        return outputDetailService.createOutputDetail(outputDetailRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/qr-scan")
    @ResponseStatus(HttpStatus.CREATED)
    void qrScan(@RequestBody QRScanRequest qrScanRequest){

    }

}

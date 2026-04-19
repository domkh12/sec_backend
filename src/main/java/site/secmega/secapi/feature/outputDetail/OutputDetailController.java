package site.secmega.secapi.feature.outputDetail;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.secmega.secapi.feature.outputDetail.dto.QRScanRequest;

@RestController
@RequestMapping("/api/v1/output-details")
@RequiredArgsConstructor
public class OutputDetailController {

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/qr-scan")
    @ResponseStatus(HttpStatus.CREATED)
    void qrScan(@RequestBody QRScanRequest qrScanRequest){

    }

}

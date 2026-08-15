package site.secmega.secapi.feature.outputDetail;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.secmega.secapi.feature.outputDetail.dto.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/v1/output-details")
@RequiredArgsConstructor
public class OutputDetailController {

    private final OutputDetailService outputDetailService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_WAREHOUSE')")
    @GetMapping("/report-excel")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<InputStreamResource> getReportOutputDetail(@ModelAttribute OutputFilterRequest outputFilterRequest) throws IOException {
        return outputDetailService.getReportOutputDetail(outputFilterRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/last48hrs")
    @ResponseStatus(HttpStatus.OK)
    List<OutputLast48Hrs> outputLast48Hrs(){
        return outputDetailService.outputLast48Hrs();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void update(@PathVariable Long id, @RequestParam Integer qty){
        outputDetailService.updateQty(id, qty);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void delete(@PathVariable Long id){
        outputDetailService.delete(id);
    }

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

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<OutputDetailResponse> findAll(@ModelAttribute OutputFilterRequest outputFilterRequest){
        return outputDetailService.findAll(outputFilterRequest);
    }

}

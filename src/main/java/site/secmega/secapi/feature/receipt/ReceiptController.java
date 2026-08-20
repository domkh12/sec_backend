package site.secmega.secapi.feature.receipt;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.secmega.secapi.feature.receipt.dto.ReceiptFilterRequest;
import site.secmega.secapi.feature.receipt.dto.ReceiptRequest;
import site.secmega.secapi.feature.receipt.dto.ReceiptResponse;

@RestController
@RequestMapping("/api/v1/receipt")
@RequiredArgsConstructor
public class ReceiptController {

    private final ReceiptService receiptService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WAREHOUSE')")
    @DeleteMapping("/{uuid}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteReceipt(@PathVariable String uuid){
        receiptService.deleteReceipt(uuid);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WAREHOUSE')")
    @PutMapping("/{uuid}")
    @ResponseStatus(HttpStatus.CREATED)
    ReceiptResponse updateReceipt(@PathVariable String uuid, @Valid @RequestBody ReceiptRequest receiptRequest){
        return receiptService.updateReceipt(uuid, receiptRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WAREHOUSE')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ReceiptResponse createReceipt(@RequestBody ReceiptRequest receiptRequest){
        return receiptService.createReceipt(receiptRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_WAREHOUSE')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<ReceiptResponse> findAll(@ModelAttribute ReceiptFilterRequest receiptFilterRequest){
        return receiptService.findAll(receiptFilterRequest);
    }

}

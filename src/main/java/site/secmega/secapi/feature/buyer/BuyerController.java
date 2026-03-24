package site.secmega.secapi.feature.buyer;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.secmega.secapi.feature.buyer.dto.BuyerFilterRequest;
import site.secmega.secapi.feature.buyer.dto.BuyerRequest;
import site.secmega.secapi.feature.buyer.dto.BuyerResponse;
import site.secmega.secapi.feature.buyer.dto.BuyerStatsResponse;

@RestController
@RequestMapping("/api/v1/buyers")
@RequiredArgsConstructor
public class BuyerController {
    private final BuyerService buyerService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}/file-upload")
    @ResponseStatus(HttpStatus.CREATED)
    BuyerResponse uploadBuyerFile(@PathVariable Long id, @RequestBody BuyerRequest buyerRequest){
        return buyerService.uploadBuyerFile(id, buyerRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    BuyerStatsResponse getBuyerStats(){
        return buyerService.getBuyerStats();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteBuyer(@PathVariable Long id){
        buyerService.deleteBuyer(id);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    BuyerResponse updateBuyer(@PathVariable Long id, @Valid @RequestBody BuyerRequest buyerRequest){
        return buyerService.updateBuyer(id, buyerRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<BuyerResponse> findAll(@ModelAttribute BuyerFilterRequest buyerFilterRequest){
        return buyerService.findAll(buyerFilterRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    BuyerResponse createBuyer(@Valid @RequestBody BuyerRequest buyerRequest){
        return buyerService.createBuyer(buyerRequest);
    }

}

package site.secmega.secapi.feature.buyer;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.secmega.secapi.feature.buyer.dto.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/buyers")
@RequiredArgsConstructor
public class BuyerController {
    private final BuyerService buyerService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("/{id}/files")
    @ResponseStatus(HttpStatus.OK)
    BuyerFileResponse getBuyerFile(@PathVariable Long id){
        return buyerService.getBuyerFile(id);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PutMapping("/{id}/file-upload")
    @ResponseStatus(HttpStatus.CREATED)
    BuyerResponse uploadBuyerFile(@PathVariable Long id, @RequestBody BuyerRequest buyerRequest){
        return buyerService.uploadBuyerFile(id, buyerRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    BuyerStatsResponse getBuyerStats(){
        return buyerService.getBuyerStats();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteBuyer(@PathVariable Long id){
        buyerService.deleteBuyer(id);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    BuyerResponse updateBuyer(@PathVariable Long id, @Valid @RequestBody BuyerRequest buyerRequest){
        return buyerService.updateBuyer(id, buyerRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<BuyerResponse> findAll(@ModelAttribute BuyerFilterRequest buyerFilterRequest){
        return buyerService.findAll(buyerFilterRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    BuyerResponse createBuyer(@Valid @RequestBody BuyerRequest buyerRequest){
        return buyerService.createBuyer(buyerRequest);
    }

}

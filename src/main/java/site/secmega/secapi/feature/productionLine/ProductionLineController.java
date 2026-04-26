package site.secmega.secapi.feature.productionLine;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import site.secmega.secapi.feature.productionLine.dto.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/production-lines")
@RequiredArgsConstructor
public class ProductionLineController {

    private final ProductionLineService productionLineService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER')")
    @GetMapping("/lookup")
    @ResponseStatus(HttpStatus.OK)
    List<ProductionLineLookupResponse> getProductionLineLookup(){
        return productionLineService.getProductionLineLookup();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteProductionLine(@PathVariable Long id){
        productionLineService.deleteProductionLine(id);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    ProductionLineResponse updateProductionLine(@PathVariable Long id, @Valid @RequestBody ProductionLineRequest productionLineRequest){
        return productionLineService.updateProductionLine(id, productionLineRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    ProductionLineResponse createProductionLine(@Valid @RequestBody ProductionLineRequest productionLineRequest){
        return productionLineService.createProductionLine(productionLineRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<ProductionLineResponse> getProductionLine(@ModelAttribute ProductionLineFilterRequest productionLineFilterRequest){
        return productionLineService.getProductionLine(productionLineFilterRequest);
    }

}

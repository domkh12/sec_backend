package site.secmega.secapi.feature.material;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.secmega.secapi.feature.material.dto.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/materials")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService materialService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("{id}/stock-in")
    @ResponseStatus(HttpStatus.OK)
    Page<StockInResponse> getStockIn(@PathVariable Long id, @ModelAttribute StockInFilterRequest stockInFilterRequest){
        return materialService.getStockIn(id, stockInFilterRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping("/stock-in")
    @ResponseStatus(HttpStatus.CREATED)
    StockInResponse stockIn(@RequestBody @Valid StockInRequest stockInRequest) {
        return materialService.stockIn(stockInRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<MaterialResponse> findAll(@ModelAttribute MaterialFilterRequest materialFilterRequest){
        return materialService.findAll(materialFilterRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    MaterialResponse createMaterial(@RequestBody @Valid MaterialRequest materialRequest) throws IOException {
        return materialService.createMaterial(materialRequest);
    }

}

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
    @PostMapping("/stock-out")
    @ResponseStatus(HttpStatus.CREATED)
    StockOutResponse stockOut(@RequestBody @Valid StockOutRequest stockOutRequest) {
        return materialService.stockOut(stockOutRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/{id}/stock-out")
    @ResponseStatus(HttpStatus.OK)
    Page<StockOutResponse> getStockOut(@PathVariable Long id, @ModelAttribute StockOutFilterRequest stockOutFilterRequest){
        return materialService.getStockOut(id, stockOutFilterRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    MaterialStatResponse getMaterialStat(){
        return materialService.getMaterialStat();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/{id}/stock-in")
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

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    MaterialResponse updateMaterial(@PathVariable Long id, @RequestBody @Valid MaterialRequest materialRequest){
        return materialService.updateMaterial(id, materialRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteMaterial(@PathVariable Long id){
        materialService.deleteMaterial(id);
    }
}

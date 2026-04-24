package site.secmega.secapi.feature.material;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.secmega.secapi.feature.material.dto.*;

import java.io.File;
import java.io.IOException;

@RestController
@RequestMapping("/api/v1/materials")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService materialService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_WAREHOUSE')")
    @GetMapping("/{id}/report-stock-out-excel")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<InputStreamResource> getReportStockOut(@PathVariable Long id) throws IOException {
        return materialService.getReportStockOut(id);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_WAREHOUSE')")
    @GetMapping("/{id}/report-stock-in-excel")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<InputStreamResource> getReportStockIn(@PathVariable Long id) throws IOException {
        return materialService.getReportStockIn(id);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_WAREHOUSE')")
    @GetMapping("/report-excel")
    @ResponseStatus(HttpStatus.OK)
    ResponseEntity<InputStreamResource> getReportMaterial() throws IOException {
        return materialService.getReportMaterial();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_WAREHOUSE')")
    @PostMapping("/stock-out")
    @ResponseStatus(HttpStatus.CREATED)
    StockOutResponse stockOut(@RequestBody @Valid StockOutRequest stockOutRequest) {
        return materialService.stockOut(stockOutRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_WAREHOUSE')")
    @GetMapping("/{id}/stock-out")
    @ResponseStatus(HttpStatus.OK)
    Page<StockOutResponse> getStockOut(@PathVariable Long id, @ModelAttribute StockOutFilterRequest stockOutFilterRequest){
        return materialService.getStockOut(id, stockOutFilterRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_WAREHOUSE')")
    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    MaterialStatResponse getMaterialStat(){
        return materialService.getMaterialStat();
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_WAREHOUSE')")
    @GetMapping("/{id}/stock-in")
    @ResponseStatus(HttpStatus.OK)
    Page<StockInResponse> getStockIn(@PathVariable Long id, @ModelAttribute StockInFilterRequest stockInFilterRequest){
        return materialService.getStockIn(id, stockInFilterRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_WAREHOUSE')")
    @PostMapping("/stock-in")
    @ResponseStatus(HttpStatus.CREATED)
    StockInResponse stockIn(@RequestBody @Valid StockInRequest stockInRequest) {
        return materialService.stockIn(stockInRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_WAREHOUSE')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<MaterialResponse> findAll(@ModelAttribute MaterialFilterRequest materialFilterRequest){
        return materialService.findAll(materialFilterRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_WAREHOUSE')")
    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    MaterialResponse createMaterial(@RequestBody @Valid MaterialRequest materialRequest) throws IOException {
        return materialService.createMaterial(materialRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_WAREHOUSE')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    MaterialResponse updateMaterial(@PathVariable Long id, @RequestBody @Valid MaterialRequest materialRequest){
        return materialService.updateMaterial(id, materialRequest);
    }

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_WAREHOUSE')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteMaterial(@PathVariable Long id){
        materialService.deleteMaterial(id);
    }
}

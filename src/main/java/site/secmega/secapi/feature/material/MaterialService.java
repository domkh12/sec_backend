package site.secmega.secapi.feature.material;

import jakarta.validation.Valid;
import org.springframework.core.io.InputStreamResource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import site.secmega.secapi.feature.material.dto.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface MaterialService {
    MaterialResponse createMaterial(@Valid MaterialRequest materialRequest) throws IOException;

    Page<MaterialResponse> findAll(MaterialFilterRequest materialFilterRequest);

    StockInResponse stockIn(@Valid StockInRequest stockInRequest);

    Page<StockInResponse> getStockIn(Long id, StockInFilterRequest stockInFilterRequest);

    MaterialStatResponse getMaterialStat();

    StockOutResponse stockOut(@Valid StockOutRequest stockOutRequest);

    Page<StockOutResponse> getStockOut(Long id, StockOutFilterRequest stockOutFilterRequest);

    MaterialResponse updateMaterial(Long id, @Valid MaterialRequest materialRequest);

    void deleteMaterial(Long id);

    ResponseEntity<InputStreamResource> getReportMaterial() throws IOException;

    ResponseEntity<InputStreamResource> getReportStockIn() throws IOException;
}

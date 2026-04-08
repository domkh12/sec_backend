package site.secmega.secapi.feature.material;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import site.secmega.secapi.feature.material.dto.*;

import java.io.IOException;

public interface MaterialService {
    MaterialResponse createMaterial(@Valid MaterialRequest materialRequest) throws IOException;

    Page<MaterialResponse> findAll(MaterialFilterRequest materialFilterRequest);

    StockInResponse stockIn(@Valid StockInRequest stockInRequest);

    Page<StockInResponse> getStockIn(Long id, StockInFilterRequest stockInFilterRequest);
}

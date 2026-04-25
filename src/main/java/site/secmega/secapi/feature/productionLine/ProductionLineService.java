package site.secmega.secapi.feature.productionLine;

import org.springframework.data.domain.Page;
import site.secmega.secapi.feature.productionLine.dto.ProductionLineFilterRequest;
import site.secmega.secapi.feature.productionLine.dto.ProductionLineLookupResponse;
import site.secmega.secapi.feature.productionLine.dto.ProductionLineRequest;
import site.secmega.secapi.feature.productionLine.dto.ProductionLineResponse;

import java.util.List;

public interface ProductionLineService {
    void deleteProductionLine(Long id);

    ProductionLineResponse updateProductionLine(Long id, ProductionLineRequest productionLineRequest);

    Page<ProductionLineResponse> getProductionLine(ProductionLineFilterRequest productionLineFilterRequest);

    ProductionLineResponse createProductionLine(ProductionLineRequest productionLineRequest);

    List<ProductionLineLookupResponse> getProductionLineLookup();
}

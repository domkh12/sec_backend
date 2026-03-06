package site.secmega.secapi.feature.productionLine;

import org.springframework.data.domain.Page;
import site.secmega.secapi.feature.productionLine.dto.ProductionLineRequest;
import site.secmega.secapi.feature.productionLine.dto.ProductionLineResponse;

public interface ProductionLineService {
    void deleteProductionLine(Long id);

    ProductionLineResponse updateProductionLine(Long id, ProductionLineRequest productionLineRequest);

    Page<ProductionLineResponse> getProductionLine(Integer pageNo, Integer pageSize);

    ProductionLineResponse createProductionLine(ProductionLineRequest productionLineRequest);
}

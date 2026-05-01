package site.secmega.secapi.feature.productionLine;

import org.springframework.data.domain.Page;
import site.secmega.secapi.feature.productionLine.dto.*;

import java.util.List;

public interface ProductionLineService {
    List<ProductionLineLookupResponse> getProductionLineByDeptNo(Integer processNo);

    List<ProductionLineLookupResponse> getProductionLineByDept(Long id);

    void deleteProductionLine(Long id);

    ProductionLineResponse updateProductionLine(Long id, ProductionLineRequest productionLineRequest);

    Page<ProductionLineResponse> getProductionLine(ProductionLineFilterRequest productionLineFilterRequest);

    ProductionLineResponse createProductionLine(ProductionLineRequest productionLineRequest);

    List<ProductionLineLookupResponse> getProductionLineLookup();
}

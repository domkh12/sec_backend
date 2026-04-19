package site.secmega.secapi.feature.outputDetail.dto;

import java.util.List;

public record QRScanRequest(
        String bundleId,
        Long workOrderId,
        Long productionLineId,
        Long sizeId,
        Integer goodQty,
        Integer defectQty,
        List<Long> defectTypeIds
) {
}

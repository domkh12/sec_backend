package site.secmega.secapi.feature.material.dto;

public record MaterialStatResponse(
        long totalMaterial,
        long totalStockIn,
        long totalStockOut,
        long totalBalance
) {
}

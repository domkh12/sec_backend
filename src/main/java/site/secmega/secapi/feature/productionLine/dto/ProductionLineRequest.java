package site.secmega.secapi.feature.productionLine.dto;

public record ProductionLineRequest(
        String name,
        Long deptId
) {
}

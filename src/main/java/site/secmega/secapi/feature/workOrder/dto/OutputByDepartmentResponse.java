package site.secmega.secapi.feature.workOrder.dto;

public record OutputByDepartmentResponse(
        Long id,
        String name,
        Integer output
) {
}

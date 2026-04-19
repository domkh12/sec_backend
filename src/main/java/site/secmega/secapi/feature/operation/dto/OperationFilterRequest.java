package site.secmega.secapi.feature.operation.dto;

public record OperationFilterRequest(
        Integer pageNo,
        Integer pageSize,
        String search
) {
    public OperationFilterRequest {
        if (pageNo == null) pageNo = 1;
        if (pageSize == null) pageSize = 20;
    }
}

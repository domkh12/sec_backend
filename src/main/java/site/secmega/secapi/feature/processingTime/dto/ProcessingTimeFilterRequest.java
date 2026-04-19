package site.secmega.secapi.feature.processingTime.dto;

public record ProcessingTimeFilterRequest(
        Integer pageNo,
        Integer pageSize,
        String search
) {
    public ProcessingTimeFilterRequest{
        if (pageNo == null) pageNo = 1;
        if (pageSize == null) pageSize = 20;
    }
}

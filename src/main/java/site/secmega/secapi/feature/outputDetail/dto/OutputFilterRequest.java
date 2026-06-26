package site.secmega.secapi.feature.outputDetail.dto;

public record OutputFilterRequest(
        Integer pageNo,
        Integer pageSize,
        String search
) {
    public OutputFilterRequest{
        if (pageNo == null){
            pageNo = 1;
        }
        if (pageSize == null){
            pageSize = 20;
        }
    }
}

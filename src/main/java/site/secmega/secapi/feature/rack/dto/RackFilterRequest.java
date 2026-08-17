package site.secmega.secapi.feature.rack.dto;

public record RackFilterRequest(
        Integer pageNo,
        Integer pageSize,
        String search
) {
    public RackFilterRequest{
        if (pageNo == null){
            pageNo = 1;
        }
        if (pageSize == null){
            pageSize = 20;
        }
    }
}

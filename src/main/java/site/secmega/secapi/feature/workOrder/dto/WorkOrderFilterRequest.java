package site.secmega.secapi.feature.workOrder.dto;

public record WorkOrderFilterRequest(
        Integer pageNo,
        Integer pageSize,
        String search
) {
    public WorkOrderFilterRequest{
        if (pageNo == null){
            pageNo = 1;
        }
        if (pageSize == null){
            pageSize = 20;
        }
    }
}

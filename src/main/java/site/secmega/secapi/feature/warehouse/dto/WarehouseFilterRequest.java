package site.secmega.secapi.feature.warehouse.dto;

public record WarehouseFilterRequest(
        Integer pageNo,
        Integer pageSize,
        String search
) {
    public WarehouseFilterRequest{
        if (pageNo == null){
            pageNo = 1;
        }
        if (pageSize == null){
            pageSize = 20;
        }
    }
}

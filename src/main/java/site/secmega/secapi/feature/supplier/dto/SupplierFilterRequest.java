package site.secmega.secapi.feature.supplier.dto;

public record SupplierFilterRequest(
        Integer pageNo,
        Integer pageSize,
        String search
) {
    public SupplierFilterRequest{
        if (pageNo == null){
            pageNo = 1;
        }
        if (pageSize == null){
            pageSize = 20;
        }
    }
}

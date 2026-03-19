package site.secmega.secapi.feature.department.dto;

public record DepartmentFilterRequest(
        Integer pageNo,
        Integer pageSize,
        String search
) {
    public DepartmentFilterRequest{
        if (pageNo == null){
            pageNo = 1;
        }
        if (pageSize == null){
            pageSize = 20;
        }
    }
}

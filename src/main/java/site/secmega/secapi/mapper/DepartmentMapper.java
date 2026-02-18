package site.secmega.secapi.mapper;

import org.mapstruct.Mapper;
import site.secmega.secapi.domain.Department;
import site.secmega.secapi.feature.department.dto.DepartmentRequest;
import site.secmega.secapi.feature.department.dto.DepartmentResponse;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    Department fromDepartmentRequest(DepartmentRequest departmentRequest);
    DepartmentResponse toDepartmentResponse(Department department);
}

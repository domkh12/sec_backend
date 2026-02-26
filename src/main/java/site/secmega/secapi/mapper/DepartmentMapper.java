package site.secmega.secapi.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import site.secmega.secapi.domain.Department;
import site.secmega.secapi.feature.department.dto.DepartmentRequest;
import site.secmega.secapi.feature.department.dto.DepartmentResponse;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    Department fromDepartmentRequest(DepartmentRequest departmentRequest);
    DepartmentResponse toDepartmentResponse(Department department);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDepartmentRequest(DepartmentRequest departmentRequest,@MappingTarget Department department);
}

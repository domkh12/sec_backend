package site.secmega.secapi.mapper;

import org.mapstruct.*;
import site.secmega.secapi.domain.Department;
import site.secmega.secapi.feature.department.dto.DepartmentRequest;
import site.secmega.secapi.feature.department.dto.DepartmentResponse;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {
    Department fromDepartmentRequest(DepartmentRequest departmentRequest);
    @Mapping(target = "lines", expression = "java(department.getProductionLines() != null ? department.getProductionLines().size() : null)")
    @Mapping(target = "workers", expression = "java(department.getUsers() != null ? department.getUsers().size() : null)")
    DepartmentResponse toDepartmentResponse(Department department);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromDepartmentRequest(DepartmentRequest departmentRequest,@MappingTarget Department department);
}

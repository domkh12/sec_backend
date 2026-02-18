package site.secmega.secapi.feature.department;

import site.secmega.secapi.feature.department.dto.DepartmentRequest;
import site.secmega.secapi.feature.department.dto.DepartmentResponse;

public interface DepartmentService {
    DepartmentResponse createDept(DepartmentRequest deptRequest);
}

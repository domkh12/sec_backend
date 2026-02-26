package site.secmega.secapi.feature.department;

import org.springframework.data.domain.Page;
import site.secmega.secapi.feature.department.dto.DepartmentRequest;
import site.secmega.secapi.feature.department.dto.DepartmentResponse;

public interface DepartmentService {
    DepartmentResponse createDept(DepartmentRequest deptRequest);

    Page<DepartmentResponse> findAllDept(Integer pageSize, Integer pageNo);

    DepartmentResponse updateDept(Long id, DepartmentRequest deptRequest);

    void deleteDept(Long id);
}

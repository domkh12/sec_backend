package site.secmega.secapi.feature.department;

import org.springframework.data.domain.Page;
import site.secmega.secapi.feature.department.dto.*;

import java.util.List;

public interface DepartmentService {
    DepartmentResponse createDept(DepartmentRequest deptRequest);

    Page<DepartmentResponse> findAllDept(DepartmentFilterRequest deptFilterRequest);

    DepartmentResponse updateDept(Long id, DepartmentRequest deptRequest);

    void deleteDept(Long id);

    List<DepartmentLookupResponse> getDeptLookup();

    DepartmentStatsResponse getDeptStats();
}

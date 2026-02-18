package site.secmega.secapi.feature.department;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import site.secmega.secapi.domain.Department;
import site.secmega.secapi.feature.department.dto.DepartmentRequest;
import site.secmega.secapi.feature.department.dto.DepartmentResponse;
import site.secmega.secapi.mapper.DepartmentMapper;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService{

    private final DepartmentMapper departmentMapper;
    private final DepartmentRepository departmentRepository;

    @Override
    public DepartmentResponse createDept(DepartmentRequest deptRequest) {
        Department dept = departmentMapper.fromDepartmentRequest(deptRequest);
        dept.setCreatedAt(LocalDateTime.now());
        dept.setUpdatedAt(LocalDateTime.now());
        dept.setIsActive(true);
        Department savedDept = departmentRepository.save(dept);
        return departmentMapper.toDepartmentResponse(savedDept);
    }

}

package site.secmega.secapi.feature.department;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.domain.Department;
import site.secmega.secapi.feature.department.dto.DepartmentRequest;
import site.secmega.secapi.feature.department.dto.DepartmentResponse;
import site.secmega.secapi.mapper.DepartmentMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService{

    private final DepartmentMapper departmentMapper;
    private final DepartmentRepository departmentRepository;

    @Override
    public void deleteDept(Long id) {
        Department dept = departmentRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found!")
        );
        departmentRepository.delete(dept);
    }

    @Override
    public DepartmentResponse updateDept(Long id, DepartmentRequest deptRequest) {
        Department dept = departmentRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found!")
        );
        departmentMapper.updateFromDepartmentRequest(deptRequest, dept);
        dept.setUpdatedAt(LocalDateTime.now());
        Department updatedDept = departmentRepository.save(dept);
        return departmentMapper.toDepartmentResponse(updatedDept);
    }

    @Override
    public Page<DepartmentResponse> findAllDept(Integer pageSize, Integer pageNo) {
        if (pageNo <= 0 || pageSize <= 0 ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "PageNo or PageSize must bigger than 0");
        }
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(pageNo - 1, pageSize, sort);
        Page<Department> departments = departmentRepository.findAll(pageRequest);
        return departments.map(departmentMapper::toDepartmentResponse);
    }

    @Override
    public DepartmentResponse createDept(DepartmentRequest deptRequest) {
        if (departmentRepository.existsByName(deptRequest.name())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Department name already exist!");
        }
        Department dept = departmentMapper.fromDepartmentRequest(deptRequest);
        dept.setCreatedAt(LocalDateTime.now());
        dept.setUpdatedAt(LocalDateTime.now());
        dept.setIsActive(true);
        Department savedDept = departmentRepository.save(dept);
        return departmentMapper.toDepartmentResponse(savedDept);
    }

}

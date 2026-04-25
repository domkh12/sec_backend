package site.secmega.secapi.feature.department;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import site.secmega.secapi.base.DepartmentStatus;
import site.secmega.secapi.domain.Department;
import site.secmega.secapi.feature.department.dto.*;
import site.secmega.secapi.feature.productionLine.ProductionLineRepository;
import site.secmega.secapi.feature.productionLine.dto.ProductionLineLookupResponse;
import site.secmega.secapi.feature.user.UserRepository;
import site.secmega.secapi.mapper.DepartmentMapper;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DepartmentServiceImpl implements DepartmentService{

    private final DepartmentMapper departmentMapper;
    private final DepartmentRepository departmentRepository;
    private final ProductionLineRepository productionLineRepository;
    private final UserRepository userRepository;

    @Override
    public DepartmentStatsResponse getDeptStats() {
        Integer totalDept = departmentRepository.countDepartment();
        Integer totalLine = productionLineRepository.countLine();
        Integer totalWorker = userRepository.countByProductionLineNotNull();
        Integer totalActive = departmentRepository.countByStatus(DepartmentStatus.Active);
        return DepartmentStatsResponse.builder()
                .totalDept(totalDept)
                .totalLine(totalLine)
                .totalWorker(totalWorker)
                .totalActive(totalActive)
                .build();
    }

    @Override
    public void deleteDept(Long id) {
        Department dept = departmentRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found!")
        );
        dept.setDeletedAt(LocalDateTime.now());
        dept.setStatus(DepartmentStatus.Inactive);
        departmentRepository.save(dept);
    }

    @Override
    public List<DepartmentLookupResponse> getDeptLookup() {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        List<Department> departments = departmentRepository.findAll(sort);
        return departments.stream().map(department -> {
             return DepartmentLookupResponse.builder()
                    .id(department.getId())
                    .name(department.getDepartment())
                    .children(department.getProductionLines().stream().map(line ->
                            ProductionLineLookupResponse.builder()
                                    .id(line.getId())
                                    .line(line.getLine())
                                    .build()
                            ).toList())
                    .build();
        }).toList();
    }

    @Override
    public DepartmentResponse updateDept(Long id, DepartmentRequest deptRequest) {

        if (departmentRepository.existsByDepartmentIgnoreCaseAndIdNotAndDeletedAtNull(deptRequest.department(), id)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Department name already exist!");
        }

        Department dept = departmentRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Department not found!")
        );
        departmentMapper.updateFromDepartmentRequest(deptRequest, dept);
        dept.setUpdatedAt(LocalDateTime.now());
        Department updatedDept = departmentRepository.save(dept);
        return departmentMapper.toDepartmentResponse(updatedDept);
    }

    @Override
    public Page<DepartmentResponse> findAllDept(DepartmentFilterRequest departmentFilterRequest) {
        if (departmentFilterRequest.pageNo() <= 0 || departmentFilterRequest.pageSize() <= 0 ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "PageNo or PageSize must bigger than 0");
        }

        Specification<Department> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (departmentFilterRequest.search() != null){
            String searchTerm = "%" + departmentFilterRequest.search().toLowerCase() + "%";
            spec = spec.and((root, query, cb) ->
                    cb.like(cb.lower(root.get("department")), searchTerm)
            );
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(departmentFilterRequest.pageNo() - 1, departmentFilterRequest.pageSize(), sort);
        Page<Department> departments = departmentRepository.findAll(spec, pageRequest);
        return departments.map(departmentMapper::toDepartmentResponse);
    }

    @Override
    public DepartmentResponse createDept(DepartmentRequest deptRequest) {
        if (departmentRepository.existsByDepartmentIgnoreCaseAndDeletedAtNull(deptRequest.department())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Department name already exist!");
        }
        Department dept = departmentMapper.fromDepartmentRequest(deptRequest);
        dept.setCreatedAt(LocalDateTime.now());
        dept.setUpdatedAt(LocalDateTime.now());
        dept.setStatus(DepartmentStatus.Inactive);
        Department savedDept = departmentRepository.save(dept);
        return departmentMapper.toDepartmentResponse(savedDept);
    }

}

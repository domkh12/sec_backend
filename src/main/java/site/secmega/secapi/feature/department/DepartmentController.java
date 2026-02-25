package site.secmega.secapi.feature.department;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.secmega.secapi.feature.department.dto.DepartmentRequest;
import site.secmega.secapi.feature.department.dto.DepartmentResponse;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_PRODUCTION_MANAGER')")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    DepartmentResponse createDept(@RequestBody DepartmentRequest deptRequest){
        return departmentService.createDept(deptRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    Page<DepartmentResponse> findAllDept(@RequestParam(required = false, defaultValue = "1") Integer pageNo,
                                         @RequestParam(required = false, defaultValue = "20") Integer pageSize){
        return departmentService.findAllDept(pageSize, pageNo);
    }
}

package site.secmega.secapi.feature.department;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.secmega.secapi.feature.department.dto.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/stats")
    @ResponseStatus(HttpStatus.OK)
    DepartmentStatsResponse getDeptStats(){
        return departmentService.getDeptStats();
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping("/lookup")
    @ResponseStatus(HttpStatus.OK)
    List<DepartmentLookupResponse> getDeptLookup(){
        return departmentService.getDeptLookup();
    }


    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteDept(@PathVariable Long id){
        departmentService.deleteDept(id);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.CREATED)
    DepartmentResponse updateDept(@PathVariable Long id, @RequestBody DepartmentRequest deptRequest){
        return departmentService.updateDept(id, deptRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    DepartmentResponse createDept(@RequestBody DepartmentRequest deptRequest){
        return departmentService.createDept(deptRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    Page<DepartmentResponse> findAllDept(@ModelAttribute DepartmentFilterRequest departmentFilterRequest){
        return departmentService.findAllDept(departmentFilterRequest);
    }
}

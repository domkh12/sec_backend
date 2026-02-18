package site.secmega.secapi.feature.department;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import site.secmega.secapi.feature.department.dto.DepartmentRequest;
import site.secmega.secapi.feature.department.dto.DepartmentResponse;

@RestController
@RequestMapping("/api/v1/departments")
@RequiredArgsConstructor
public class DepartmentController {

    private final DepartmentService departmentService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    DepartmentResponse createDept(@RequestBody DepartmentRequest deptRequest){
        return departmentService.createDept(deptRequest);
    }
}

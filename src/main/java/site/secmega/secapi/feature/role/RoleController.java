package site.secmega.secapi.feature.role;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import site.secmega.secapi.feature.role.dto.RoleResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@RequiredArgsConstructor
public class RoleController {

    private final RoleService roleService;

    @PreAuthorize("hasAnyAuthority('ROLE_ADMIN', 'ROLE_HR_MANAGER')")
    @GetMapping
    @ResponseStatus(HttpStatus.CREATED)
    Page<RoleResponse> findAll(
            @RequestParam(required = false, defaultValue = "1") Integer pageNo,
            @RequestParam(required = false, defaultValue = "20") Integer pageSize
    ){
        return roleService.findAll(pageNo, pageSize);
    }

}

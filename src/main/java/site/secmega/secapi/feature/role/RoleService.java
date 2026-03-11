package site.secmega.secapi.feature.role;

import org.springframework.data.domain.Page;
import site.secmega.secapi.feature.role.dto.RoleResponse;

import java.util.List;

public interface RoleService {
    Page<RoleResponse> findAll(Integer pageNo, Integer pageSize);
}

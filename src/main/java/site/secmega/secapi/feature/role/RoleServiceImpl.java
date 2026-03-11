package site.secmega.secapi.feature.role;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import site.secmega.secapi.domain.Role;
import site.secmega.secapi.feature.role.dto.RoleResponse;
import site.secmega.secapi.mapper.RoleMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    public Page<RoleResponse> findAll(Integer page, Integer size) {
        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(page - 1, size, sort);
        Page<Role> roles = roleRepository.findAll(pageRequest);
        return roles.map(roleMapper::toRoleResponse);
    }
}

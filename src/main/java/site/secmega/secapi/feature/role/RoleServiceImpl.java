package site.secmega.secapi.feature.role;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import site.secmega.secapi.domain.Role;
import site.secmega.secapi.feature.role.dto.RoleFilterRequest;
import site.secmega.secapi.feature.role.dto.RoleResponse;
import site.secmega.secapi.feature.user.UserRepository;
import site.secmega.secapi.mapper.RoleMapper;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService{

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;

    @Override
    public Page<RoleResponse> findAll(RoleFilterRequest roleFilterRequest) {

        if (roleFilterRequest.pageNo() <= 0 || roleFilterRequest.pageSize() <= 0 ){
            throw new IllegalArgumentException("Page no and Page size must be greater than 0");
        }

        Specification<Role> spec = Specification.where((root, query, cb) -> cb.conjunction());

        if (roleFilterRequest.search() != null){
            String searchTerm = "%" + roleFilterRequest.search().toLowerCase() + "%";
            spec = spec.and((root, query, cb) ->
                    cb.or(
                            cb.like(cb.lower(root.get("name")), searchTerm),
                            cb.like(cb.lower(root.get("description")), searchTerm)
                    )
            );
        }

        Sort sort = Sort.by(Sort.Direction.DESC, "createdAt");
        PageRequest pageRequest = PageRequest.of(roleFilterRequest.pageNo() - 1, roleFilterRequest.pageSize(), sort);
        Page<Role> roles = roleRepository.findAll(spec, pageRequest);
        return roles.map(role -> RoleResponse.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .users(userRepository.countByRoles_Id(role.getId()))
                .build());
    }
}

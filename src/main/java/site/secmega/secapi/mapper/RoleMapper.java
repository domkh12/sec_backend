package site.secmega.secapi.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import site.secmega.secapi.domain.Role;
import site.secmega.secapi.feature.role.dto.RoleResponse;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    RoleResponse toRoleResponse(Role role);
}

package site.secmega.secapi.mapper;

import org.mapstruct.*;
import site.secmega.secapi.domain.User;
import site.secmega.secapi.feature.auth.dto.ProfileRequest;
import site.secmega.secapi.feature.auth.dto.ProfileResponse;
import site.secmega.secapi.feature.user.dto.UserLookupResponse;
import site.secmega.secapi.feature.user.dto.UserRequest;
import site.secmega.secapi.feature.user.dto.UserResponse;
import site.secmega.secapi.feature.user.dto.UserStatsResponse;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User fromUserRequest(UserRequest userRequest);
    @Mapping(target = "role", expression = "java(user.getRoles().stream().map(r -> r.getName()).findFirst().orElse(null))")
    @Mapping(target = "roleId", expression = "java(user.getRoles().stream().map(r -> r.getId()).findFirst().orElse(null))")
    @Mapping(target = "lineId", expression = "java(user.getProductionLine() != null ? user.getProductionLine().getId() : null)")
    @Mapping(target = "line", expression = "java(user.getProductionLine() != null ? user.getProductionLine().getLine() : null)")
    @Mapping(target = "department", expression = "java(user.getDepartment() != null ? user.getDepartment().getDepartment() : null)")
    @Mapping(target = "departmentId", expression = "java(user.getDepartment() != null ? user.getDepartment().getId() : null)")
    UserResponse toUserResponse(User user);
    ProfileResponse toProfileResponse(User user);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromProfileRequest(ProfileRequest profileRequest, @MappingTarget User user);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateUserFromUserRequest(UserRequest userRequest, @MappingTarget User user);
    UserLookupResponse toUserLookupResponse(User user);
}

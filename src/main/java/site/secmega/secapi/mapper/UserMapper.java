package site.secmega.secapi.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import site.secmega.secapi.domain.User;
import site.secmega.secapi.feature.auth.dto.ProfileRequest;
import site.secmega.secapi.feature.auth.dto.ProfileResponse;
import site.secmega.secapi.feature.user.dto.UserRequest;
import site.secmega.secapi.feature.user.dto.UserResponse;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User fromUserRequest(UserRequest userRequest);
    UserResponse toUserResponse(User user);
    ProfileResponse toProfileResponse(User user);
    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFromProfileRequest(ProfileRequest profileRequest, @MappingTarget User user);

}
